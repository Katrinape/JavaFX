package sample.controller;

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

public class MainController {

    @FXML
    private Button showParamsButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button addParametersButton;
    @FXML
    private TableView<Parameter> parametersTable;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void listParameters() {
        GetAllParametersTask task = new GetAllParametersTask();
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
        fxmlLoader.setLocation(getClass().getResource("/sample/fxml/addParams.fxml"));

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
            AddParamsController controller = fxmlLoader.getController();
            Parameter parameter = controller.processResults();
            if (Datasource.getInstance().addParameters(parameter.getDate(), parameter.getWeight(),
                    parameter.getTemperature())) {
                listParameters();
            }
        }
    }

    @FXML
    public void showChart() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/chart.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChartController chart = loader.getController();
        chart.show();

        this.mainBorderPane.setCenter(chart.lineChart);
    }

    @FXML
    private void deleteParameters() {
        final Parameter parameter = parametersTable.getSelectionModel().getSelectedItem();
        if (parameter == null) {
            System.out.println("Select item!!!");
            return;
        }

        if (Datasource.getInstance().deleteParameters(parameter.getDate())) {
            listParameters();
        }
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void showParameters() {
        this.mainBorderPane.setCenter(parametersTable);
    }

    private class GetAllParametersTask extends Task {

        @Override
        protected ObservableList<Parameter> call() {
            return FXCollections.observableArrayList(Datasource.getInstance().queryParameters());
        }
    }
}