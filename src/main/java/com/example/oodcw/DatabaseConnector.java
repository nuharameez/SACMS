package com.example.oodcw;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

//creating an interface
public interface DatabaseConnector {
    boolean authenticateRegistration(String role, String authenticatorA, String columnName, Connection connection);

}