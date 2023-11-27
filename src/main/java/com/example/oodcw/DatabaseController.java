package com.example.oodcw;

import java.sql.*;

public class DatabaseController {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/scams_schedule";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public boolean IDExists(int ID) {
        try (Connection connection = DatabaseController.getConnection()) {
            String query = "SELECT COUNT(*) FROM schedule WHERE ScheduleID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, ID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

