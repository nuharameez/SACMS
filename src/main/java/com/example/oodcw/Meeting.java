package com.example.oodcw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Meeting extends Schedule{
    private String description;
    private String type;

    public Meeting(int ScheduleID, String name, LocalDate date, String venue, String description) {
        super(ScheduleID,name, date, venue);
        this.description=description;
        this.type="Meeting";
    }

    public String getDescription() {

        return description;
    }
    @Override
    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public void saveToDatabase() {
        try (Connection connection = DatabaseController.getConnection()) {
            // Insert into 'schedule' table
            String scheduleQuery = "INSERT INTO schedule (ScheduleID, Name, Date, Venue, Type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement scheduleStatement = connection.prepareStatement(scheduleQuery)) {
                scheduleStatement.setInt(1, getScheduleID());
                scheduleStatement.setString(2, getName());
                scheduleStatement.setObject(3, getDate());
                scheduleStatement.setString(4, getVenue());
                scheduleStatement.setString(5, "Meeting");

                scheduleStatement.executeUpdate();
            }

            // Insert into 'meeting' table
            String meetingQuery = "INSERT INTO meeting (ScheduleID, Description) VALUES (?, ?)";
            try (PreparedStatement meetingStatement = ((Connection) connection).prepareStatement(meetingQuery)) {
                meetingStatement.setInt(1, getScheduleID());
                meetingStatement.setString(2, getDescription());

                meetingStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
