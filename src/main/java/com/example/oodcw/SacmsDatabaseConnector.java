package com.example.oodcw;
import javafx.collections.FXCollections;
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

    public boolean authenticateUser(String role, String username, String password, Connection connection) {

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
    public boolean authenticateRegistration(String role, String authenticator, String columnName, Connection connection) {

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

    public void addNewUser(String role, String id, String name, String username, String password, Connection connection) {
        String sql = "INSERT INTO " + role + " (" + role + "Id, " + role + "Name, " + role + "Username, " + role + "Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");

            try (Statement statement = connection.createStatement()) {
                String createTableSQL = "CREATE TABLE " + role + " (" + role + "Id VARCHAR(45), " + role + "Name VARCHAR(50), " + role + "Username VARCHAR(45), " + role + "Password VARCHAR(45))";
                statement.executeUpdate(createTableSQL);

                // Retry the insert after creating the table
                addNewUser(role, id, name, username, password, connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    //to populate observable arraylist with the club options.

    public void clubOptions(ObservableList<String> clubOption) {
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

    public String getClubAdvisorName(String clubName, Connection connection) {
        String sql = "SELECT clubAdvisor FROM clubstable WHERE  clubName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clubName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("clubAdvisor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getStudentCount(String clubTable, Connection connection) {
        String sql = "SELECT COUNT(*) AS studentCount FROM " + clubTable;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("studentCount");
            }
        } catch (SQLException e) {
            System.out.println("The club table does not exist");
            ;
        }
        return 0;
    }

    public ObservableList<Student> getStudents(String clubTable, Connection connection) {
        ObservableList<Student> studentData = FXCollections.observableArrayList();
        String sql = "SELECT studentId, studentName FROM " + clubTable;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String studentId = resultSet.getString("studentId");
                String studentName = resultSet.getString("studentName");
                studentData.add(new Student(studentId, studentName));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return studentData;
    }


    public UserDetails getUserDetails(String role, String username, Connection connection) {
        String query = "SELECT " + role + "Id," + role + "Name FROM " + role + "student WHERE" + role+ "Username = ?";
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


    public void addStudentToClubTable(String id, String name, String clubName, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + clubName + "(studentId, studentName) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Club table does not exist. Creating new table ... ");
            String createTableSQL = "CREATE TABLE " + clubName + " (studentId VARCHAR(45), studentName VARCHAR(50))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            }
            addStudentToClubTable(id, name, clubName, connection);
        }
    }


    public boolean authenticateJoinClub(String clubName, String id, Connection connection) {

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

        } catch (SQLException e) {
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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

}