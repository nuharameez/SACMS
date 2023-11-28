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

    public  boolean authenticateUser(String role, String username, String password, Connection connection)  {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ? AND " + role + "Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");

        }
        return false;
    }

    //checking if the id already exists.
    //if id exists the user already has an account.


    //checking if the id already exists.
    //if id exists the user already has an account.
    //check if username is unique
    @Override
    public  boolean authenticateRegistration(String role, String authenticator, String columnName, Connection connection) {

        String sql = "SELECT * FROM " + role + " WHERE " + role + columnName + " = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, authenticator);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");
        }
        return false;
    }



    //method to add new registry to the database

    public  void addNewUser(String role, String id, String name, String username, String password, Connection connection) {
        String sql = "INSERT INTO " + role + " (" + role + "Id, " + role + "Name, " + role + "Username, " + role + "Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");
        }
    }

    //to populate observable arraylist with the club options.

    public void clubOptions(ObservableList<String> clubOption)  {
        try {
            Statement statement = dbConnector().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clubName FROM clubstable");
            while (resultSet.next()) {
                String clubName = resultSet.getString("clubName");
                clubOption.add(clubName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }

    }


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



    public  void addStudentToClubTable(String id, String name, String clubName, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + clubName + "(studentId, studentName) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Club table does not exist. Creating new table ... ");
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


    public void createClub(String Name, String Category, String Advisor, String Motto, Connection connection) throws SQLException {
        // if club is not in database, create and insert
        String createClubSqlQuery = "INSERT INTO clubsTable (clubName, clubCategory, clubAdvisor, clubMotto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createClubSqlQuery)) {
            preparedStatement.setString(1, Name);
            preparedStatement.setString(2, Category);
            preparedStatement.setString(3, Advisor);
            preparedStatement.setString(4, Motto);
            // Execute query to create new club
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            String sql = "CREATE TABLE IF NOT EXISTS " + "clubstable" + " (" +
                    "clubID INT AUTO_INCREMENT PRIMARY KEY," +
                    "clubName VARCHAR(50)," +
                    "clubCategory VARCHAR(50)," +
                    "clubAdvisor VARCHAR(50)," +
                    "clubMotto VARCHAR(200)" + ")";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
            createClubSqlQuery = "INSERT INTO clubsTable (clubName, clubCategory, clubAdvisor, clubMotto) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createClubSqlQuery)) {
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, Category);
                preparedStatement.setString(3, Advisor);
                preparedStatement.setString(4, Motto);
                // Execute query to create new club
                preparedStatement.executeUpdate();

            }
        }
    }


    public void deleteClub(String Name, Connection connection) throws SQLException {
        String deleteClubSqlQuery = "DELETE FROM clubsTable WHERE clubName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteClubSqlQuery)) {
            preparedStatement.setString(1, Name);

            //execute the delete query
            preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateClub(String newClubName, String clubCategory, String clubAdvisor, String clubMotto, int clubID, Connection connection) throws SQLException {
        // create and insert
        String updateQuery = "UPDATE clubstable SET " +
                "clubName=?, " +
                "clubCategory=?, " +
                "clubAdvisor=?, " +
                "clubMotto=? " +
                "WHERE clubId=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newClubName);
            preparedStatement.setString(2, clubCategory);
            preparedStatement.setString(3, clubAdvisor);
            preparedStatement.setString(4, clubMotto);
            preparedStatement.setInt(5, clubID);

            // Execute the update statement
            preparedStatement.executeUpdate();
            System.out.println("Club updated successfully!");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't update club!");
        }

    }


    public boolean clubExists(String clubName, Connection connection) {

        // Check if the club exists
        String checkClubExistsQuery = "SELECT * FROM clubstable WHERE clubName = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkClubExistsQuery)) {
            checkStatement.setString(1, clubName);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                return resultSet.next(); // if the club name exists returns true if not false
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

}