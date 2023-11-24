package com.example.oodcw;
import javafx.collections.ObservableList;


import java.sql.*;

public class SacmsDatabaseConnector {
    public static Connection dbConnector() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sacms",
                    "root",
                    "z518@gh34abde158"
            );
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return null;
    }

        //get explanation on how this works
    //this method used in register class will malfunction since it also checks if the password is the same, we need to check only the id
    public static boolean authenticateUser(String role, String username, String password, Connection connection)  {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ? AND " + role + "Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return false;
    }

    public static boolean authenticateRegistration(String role, String id, Connection connection) {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return false;
    }

    public static boolean authenticateUsername(String role, String username, Connection connection){

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return false;
    }

    public static void addNewUser(String role, String id, String username, String password, Connection connection) {
        String sql = "INSERT INTO " + role + " (" + role + "Id, " + role + "Username, " + role + "Password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
    }

    public static boolean authenticateId(String role, String id, Connection connection)  {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return false;
    }

    static void clubOptions(ObservableList<String> clubOption)  {
        try {
            Statement statement = dbConnector().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clubName FROM club");
            while (resultSet.next()) {
                String clubName = resultSet.getString("clubName");
                clubOption.add(clubName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }

    }
}
