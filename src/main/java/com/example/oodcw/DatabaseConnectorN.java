package com.example.oodcw;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface DatabaseConnectorN {
    boolean isDateAlreadyScheduled(LocalDate date);
    boolean IDExists(int ID);
    void saveToDatabase();
    static List<Schedule> getDataFromDatabase() throws SQLException {
        return null;
    }

}
