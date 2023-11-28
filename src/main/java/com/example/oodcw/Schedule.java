package com.example.oodcw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class Schedule {
    private int ScheduleID;
    private String name;
    private LocalDate date;
    private String venue;

    private String type;

    public Schedule() {
    }

    public Schedule(int ScheduleID,String name, LocalDate date, String venue) {
        this.ScheduleID=ScheduleID;
        this.name = name;
        this.date = date;
        this.venue = venue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScheduleID() {

        return ScheduleID;
    }

    public void setScheduleID(int scheduleID) {

        ScheduleID = scheduleID;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public LocalDate getDate() {

        return date;
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }

    public String getVenue() {

        return venue;
    }

    public void setVenue(String venue) {

        this.venue = venue;
    }
    public boolean IDExists(int meetingID) {
        try (Connection connection = DatabaseController.getConnection()) {
            String query = "SELECT COUNT(*) FROM schedule WHERE ScheduleID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, meetingID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDateAlreadyScheduled(LocalDate date) {
        try (Connection connection = DatabaseController.getConnection()) {
            String query = "SELECT COUNT(*) FROM schedule WHERE Date = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, date);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public abstract void saveToDatabase();
}
