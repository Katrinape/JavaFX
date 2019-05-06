package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import sample.model.Datasource;
import sample.model.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ChartController {

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    void show() {
        List<Parameter> parameters = new ArrayList<>(Datasource.getInstance().queryParameters());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Parameter parameter : parameters) {
            series.getData().add(new XYChart.Data<>(parameter.getDate(), parameter.getWeight()));
        }
        series.setName("Waga");
        lineChart.getData().add(series);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for (Parameter parameter : parameters) {
            series1.getData().add(new XYChart.Data<>(parameter.getDate(), parameter.getTemperature()));
        }
        series1.setName("Temperatura");

        lineChart.setTitle("Wykres wagi i temperatury");
        lineChart.getData().add(series1);
    }
}
