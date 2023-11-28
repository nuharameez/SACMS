package com.example.oodcw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Event extends Schedule{
    private int maxParticipants;
    private String sponsors;
    private String details;
    private String type;
    private boolean membersOnly;

    public Event(int ScheduleID, String name, LocalDate date, String venue, int maxParticipants, String sponsors, String details, boolean membersOnly) {
        super( ScheduleID, name, date, venue);
        this.maxParticipants = maxParticipants;
        this.sponsors = sponsors;
        this.details = details;
        this.membersOnly = membersOnly;
        this.type="Event";
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

    public String getSponsors() {

        return sponsors;
    }

    public void setSponsors(String sponsors) {

        this.sponsors = sponsors;
    }

    public String getDetails() {

        return details;
    }

    public void setDetails(String details) {

        this.details = details;
    }

    public boolean isMembersOnly() {

        return membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {

        this.membersOnly = membersOnly;
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
                scheduleStatement.setString(5, "Event");

                scheduleStatement.executeUpdate();
            }

            // Insert into 'event' table
            String eventQuery = "INSERT INTO event (ScheduleID, MaxParticipants, sponsors, details, MemberOnly) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement eventStatement = connection.prepareStatement(eventQuery)) {
                eventStatement.setInt(1, getScheduleID());
                eventStatement.setInt(2, getMaxParticipants());
                eventStatement.setString(3, getSponsors());
                eventStatement.setString(4, getDetails());
                eventStatement.setString(5, isMembersOnly() ? "Yes" : "No");

                eventStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
