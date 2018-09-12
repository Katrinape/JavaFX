package sample.model;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Parameter {
    private SimpleStringProperty date;
    private SimpleDoubleProperty weight;
    private SimpleDoubleProperty temperature;

    public Parameter() {
        this.date = new SimpleStringProperty();
        this.weight = new SimpleDoubleProperty();
        this.temperature = new SimpleDoubleProperty();
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String  date) {
        this.date.set(date);
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public double getTemperature() {
        return temperature.get();
    }

    public void setTemperature(double temperature) {
        this.temperature.set(temperature);
    }
}
