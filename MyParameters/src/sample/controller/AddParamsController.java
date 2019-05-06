package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.model.Parameter;
import java.time.LocalDate;

public class AddParamsController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField weightField;
    @FXML
    private TextField temperatureField;


    Parameter processResults() {
        LocalDate date = datePicker.getValue();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date.getYear());
        stringBuilder.append("-");
        if (date.getMonthValue() < 10) stringBuilder.append("0");
        stringBuilder.append(date.getMonthValue());
        stringBuilder.append("-");
        if (date.getDayOfMonth() < 10) stringBuilder.append("0");
        stringBuilder.append(date.getDayOfMonth());
        String dateString = stringBuilder.toString();
        double weight = Double.parseDouble(weightField.getText());
        double temperature = Double.parseDouble(temperatureField.getText());

        Parameter parameter = new Parameter();
        parameter.setDate(dateString);
        parameter.setWeight(weight);
        parameter.setTemperature(temperature);

        return parameter;
    }
}
