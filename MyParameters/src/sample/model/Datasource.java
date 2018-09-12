package sample.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "parameters.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:/home/ptaku/IdeaProjects/MyParameters/" + DB_NAME;

    public static final String TABLE_PARAMETERS = "parameters";
    public static final String TABLE_PARAMETERS_DATE = "date";
    public static final String TABLE_PARAMETERS_WEIGHT = "weight";
    public static final String TABLE_PARAMETERS_TEMPERATURE = "temperature";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PARAMETERS +
            '(' + TABLE_PARAMETERS_DATE + " DATE NOT NULL, " +
            TABLE_PARAMETERS_WEIGHT + " DOUBLE, " + TABLE_PARAMETERS_TEMPERATURE + " DOUBLE)";

    public static final String QUERY_ALL_PARAMETERS = "SELECT * FROM " + TABLE_PARAMETERS +
            " ORDER BY " + TABLE_PARAMETERS_DATE;

    public static final String ADD_PARAMETERS = "INSERT INTO " + TABLE_PARAMETERS +
            '(' + TABLE_PARAMETERS_DATE + ", " +
            TABLE_PARAMETERS_WEIGHT + ", " +
            TABLE_PARAMETERS_TEMPERATURE + ") VALUES(?, ?, ?)";

    public static final String DELETE_PARAMETERS = "DELETE FROM " + TABLE_PARAMETERS +
            " WHERE " + TABLE_PARAMETERS_DATE + " = \"";

    private Connection connection;

    private PreparedStatement addParameters;

    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try  {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            addParameters = connection.prepareStatement(ADD_PARAMETERS);
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (addParameters != null) {
                addParameters.close();
            }

            if (connection != null) {
                connection.close();
            }

        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);

        } catch (SQLException e) {
            System.out.println("Create table failed: " + e.getMessage());
        }
    }

    public List<Parameter> queryParameters() {
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_ALL_PARAMETERS)) {

            List<Parameter> parameters = new ArrayList<>();
            while (resultSet.next()) {
                Parameter parameter = new Parameter();
                parameter.setDate(resultSet.getString(1));
                parameter.setWeight(resultSet.getDouble(2));
                parameter.setTemperature(resultSet.getDouble(3));
                parameters.add(parameter);
            }

            return parameters;

        } catch (SQLException e) {
            System.out.println("Couldn't get parameters: " + e.getMessage());
            return null;
        }
    }

    public boolean addParameters(String date, double weight, double temperature) {

        try {
            addParameters.setString(1, date);
            addParameters.setDouble(2, weight);
            addParameters.setDouble(3, temperature);

            addParameters.execute();

            return true;

        } catch (SQLException e) {
            System.out.println("Add Parameters failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteParameters(String date) {

        StringBuilder stringBuilder = new StringBuilder(DELETE_PARAMETERS);
        stringBuilder.append(date);
        stringBuilder.append("\"");

        try (Statement statement = connection.createStatement()) {
            statement.execute(stringBuilder.toString());
            return true;

        } catch (SQLException e) {
            System.out.println(stringBuilder.toString());
            System.out.println("Delete Parameters failed: " + e.getMessage());
            return false;
        }
    }
}
