package com.example.oodcw;
import javafx.collections.ObservableList;


import java.sql.*;

public class SacmsDatabaseConnector implements DatabaseConnector {

    //creating a database connector
    public Connection dbConnector() {
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


    //this method used in register class will malfunction since it also checks if the password is the same,
    // we need to check only the id

    //authenticating the user for login
    @Override
    public  boolean authenticateUser(String role, String username, String password, Connection connection)  {

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

    //checking if the id already exists.
    //if id exists the user already has an account.
    @Override
    public  boolean authenticateRegistration(String role, String id, Connection connection) {

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

    //checking if the username is unique
    @Override
    public  boolean authenticateUsername(String role, String username, Connection connection){

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

    //method to add new registry to the database
    @Override
    public  void addNewUser(String role, String id, String name, String username, String password, Connection connection) {
        String sql = "INSERT INTO " + role + " (" + role + "Id, " + role + "Name, " + role + "Username, " + role + "Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to add user to database");
        }
    }

    //to populate observable arraylist with the club options.
    @Override
    public void clubOptions(ObservableList<String> clubOption)  {
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

    @Override
    public  UserDetails getUserDetails(String username, Connection connection) {
        String query = "SELECT studentId, studentName FROM student WHERE  studentUsername = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString("studentId");
                String name = resultSet.getString("studentName");
                return new UserDetails(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public  void addStudentToClubTable(String id, String name, String clubName, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + clubName + "(studentId, studentName) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add to club table");
            String createTableSQL = "CREATE TABLE " + clubName + " (studentId VARCHAR(255), studentName VARCHAR(255))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            }
            sql = "INSERT INTO " + clubName + "(studentId, studentName) VALUES (?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);

                preparedStatement.executeUpdate();
            }
        }
    }


    @Override
    public  boolean authenticateJoinClub(String clubName, String id, Connection connection) {

        String sql = "SELECT * FROM " + clubName + " WHERE studentId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return false;
    }

}