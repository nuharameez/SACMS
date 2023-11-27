package com.example.oodcw;
import javafx.fxml.Initializable;

import java.sql.*;

public class ScamsDatabaseConnectorNew implements DatabaseConnectorNew {

    public Connection dbConnectorNew() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/scams",
                    "root",
                    "strawberry@2002"
            );
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return null;
    }

    @Override
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
            e.printStackTrace();
        }
    }

    @Override
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

    @Override
    public void updateClub(String newClubName, String clubCategory, String clubAdvisor, String clubMotto, int clubID, Connection connection) throws SQLException {
        // create and insert
        String updateQuery = "UPDATE clubstable SET " +
                "clubName=?, " +
                "clubCategory=?, " +
                "clubAdvisor=?, " +
                "clubMotto=? " +
                "WHERE clubID=?";

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


    @Override
    public boolean clubExists(String clubName, Connection connection) {

        // Check if the club exists
        String checkClubExistsQuery = "SELECT * FROM clubsTable WHERE clubName = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkClubExistsQuery)) {
            checkStatement.setString(1, clubName);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                return resultSet.next(); // if the club name exists returns true if not false
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

