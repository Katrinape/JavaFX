package sample.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private static final String DB_NAME = "parameters.db";

    private static final String CONNECTION_STRING =
            String.format("jdbc:sqlite:/home/ptaku/IdeaProjects/MyParameters/%s", DB_NAME);

    private static final String TABLE_PARAMETERS = "parameters";
    private static final String TABLE_PARAMETERS_DATE = "date";
    private static final String TABLE_PARAMETERS_WEIGHT = "weight";
    private static final String TABLE_PARAMETERS_TEMPERATURE = "temperature";

    private static final String CREATE_TABLE =
            String.format("CREATE TABLE IF NOT EXISTS %s(%s DATE NOT NULL, %s DOUBLE, %s DOUBLE)",
                    TABLE_PARAMETERS, TABLE_PARAMETERS_DATE, TABLE_PARAMETERS_WEIGHT, TABLE_PARAMETERS_TEMPERATURE);

    private static final String QUERY_ALL_PARAMETERS =
            String.format("SELECT * FROM %s ORDER BY %s",
                    TABLE_PARAMETERS, TABLE_PARAMETERS_DATE);

    private static final String ADD_PARAMETERS =
            String.format("INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)",
                    TABLE_PARAMETERS, TABLE_PARAMETERS_DATE, TABLE_PARAMETERS_WEIGHT, TABLE_PARAMETERS_TEMPERATURE);

    private static final String DELETE_PARAMETERS =
            String.format("DELETE FROM %s WHERE %s = \"",
                    TABLE_PARAMETERS, TABLE_PARAMETERS_DATE);

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
