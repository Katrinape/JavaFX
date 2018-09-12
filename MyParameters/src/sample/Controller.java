package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import sample.model.Datasource;
import sample.model.Parameter;
import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    TableView parametersTable;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    Button closeButton;

    @FXML
    Button addParametersButton;

    @FXML
    public void listParameters() {
        Task<ObservableList<Parameter>> task = new GetAllParametersTask();
        parametersTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    public void addParametersOpen() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add Parameters");
        dialog.setHeaderText("Dodaj swoje pomiary");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addParameters.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            AddParametersController controller = fxmlLoader.getController();
            Parameter parameter = controller.processResults();
            if (Datasource.getInstance().addParameters(parameter.getDate(), parameter.getWeight(),
                    parameter.getTemperature())) {
                listParameters();
            }
        }
    }

    @FXML
    public void showChart() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Wykres");
        dialog.setHeaderText("Wykres wagi i temperatury");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chart.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        Chart chart = loader.getController();
        chart.show();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    public void deleteParameters() {
        final Parameter parameter = (Parameter) parametersTable.getSelectionModel().getSelectedItem();
        if (parameter == null) {
            System.out.println("Select item!!!");
            return;
        }

        if (Datasource.getInstance().deleteParameters(parameter.getDate())) {
            listParameters();
        }
    }

    @FXML
    public void close() {
        Platform.exit();
    }
}

class GetAllParametersTask extends Task {

    @Override
    protected ObservableList<Parameter> call() {
        return FXCollections.observableArrayList(
                Datasource.getInstance().queryParameters()
        );
    }
}
