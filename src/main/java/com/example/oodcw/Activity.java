package com.example.oodcw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Activity extends Schedule {
    private int maxParticipants;
    private String description;

    private String type;


    public Activity(int ScheduleID, String name, LocalDate date, String venue, int maxParticipants, String description) {
        super( ScheduleID,name, date, venue);
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.type= "Activity";
    }

    @Override
    public String getType() {
        return type;
    }


    public int getMaxParticipants() {

        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {

        this.maxParticipants = maxParticipants;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public void ActivitysaveToDatabase() {
        try (Connection connection = DatabaseController.getConnection()) {
            // Insert into 'schedule' table
            String scheduleQuery = "INSERT INTO schedule (ScheduleID, Name, Date, Venue, Type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement scheduleStatement = connection.prepareStatement(scheduleQuery)) {
                scheduleStatement.setInt(1, getScheduleID());
                scheduleStatement.setString(2, getName());
                scheduleStatement.setObject(3, getDate());
                scheduleStatement.setString(4, getVenue());
                scheduleStatement.setString(5, "Activity"); // Set type to 'Activity'

                scheduleStatement.executeUpdate();
            }

            // Insert into 'activity' table
            String activityQuery = "INSERT INTO activity (ScheduleID, MaxParticipants, Description) VALUES (?, ?, ?)";
            try (PreparedStatement activityStatement = ((Connection) connection).prepareStatement(activityQuery)) {
                activityStatement.setInt(1, getScheduleID());
                activityStatement.setInt(2, getMaxParticipants());
                activityStatement.setString(3, getDescription());

                activityStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}