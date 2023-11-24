package com.example.oodcw;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.*;

public class SacmsDatabaseConnector {
    public static Connection dbConnector() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sacms",
                "root",
                "z518@gh34abde158"
        );
    }

        //get explanation on how this works
    //this method used in register class will malfunction since it also checks if the password is the same, we need to check only the id
    public static boolean authenticateUser(String role, String username, String password, Connection connection) throws SQLException {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ? AND " + role + "Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
    }

    public static boolean authenticateRegistration(String role, String id, Connection connection) throws SQLException {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
    }

    public static boolean authenticateUsername(String role, String username, Connection connection) throws SQLException {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public static void addNewUser(String role, String id, String username, String password, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + role + " (" + role + "Id, " + role + "Username, " + role + "Password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean authenticateId(String role, String id, Connection connection) throws SQLException {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }












}
