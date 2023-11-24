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

    public static boolean authenticateUser(String role, String username, String password, Connection connection) throws SQLException {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ? AND " + role + "Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
    }








}
