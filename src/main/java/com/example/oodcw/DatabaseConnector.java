package com.example.oodcw;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

//creating an interface
public interface DatabaseConnector {
    Connection dbConnector();
    boolean authenticateUser(String role, String username, String password, Connection connection);
    boolean authenticateRegistration(String role, String id, Connection connection);
    boolean authenticateUsername(String role, String username, Connection connection);
    void addNewUser(String role, String id, String name, String username, String password, Connection connection);
    void clubOptions(ObservableList<String> clubOption);
    UserDetails getUserDetails(String username, Connection connection);
    void addStudentToClubTable(String id, String name, String clubName, Connection connection) throws SQLException;
    boolean authenticateJoinClub(String clubName, String id, Connection connection);
}