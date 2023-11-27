package com.example.oodcw;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectorNew {
    Connection dbConnectorNew();
    void createClub(String Name, String Category, String Advisor, String Motto, Connection connection) throws SQLException;

    boolean clubExists(String clubName, Connection connection);

    void deleteClub(String Name, Connection connection) throws SQLException;

    void updateClub(String newClubName, String clubCategory, String clubAdvisor, String clubMotto, int clubID, Connection connection) throws SQLException;

}
