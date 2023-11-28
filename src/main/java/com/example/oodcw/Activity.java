package com.example.oodcw;
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


}