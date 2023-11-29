package com.example.oodcw;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Activity extends Schedule {
    private int maxParticipants;
    private String description;

    private String type;


    public Activity(int ScheduleID, String name, LocalDate date, String venue, int maxParticipants, String description, String club) {
        super( ScheduleID,name, date, venue, club);
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.type= "Activity";
    }

    public Activity(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.maxParticipants = resultSet.getInt("MaxParticipants");
        this.description = resultSet.getString("Description");
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


}