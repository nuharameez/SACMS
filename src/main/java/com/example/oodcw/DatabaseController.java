package com.example.oodcw;

import java.sql.*;

public class DatabaseController {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/scams_schedule";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}
