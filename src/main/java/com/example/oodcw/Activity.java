package com.example.oodcw;

import java.util.Date;

public class Activity extends Schedule{
    private int maxParticipants;
    private String description;

    public Activity(String name, Date date, String venue, int maxParticipants, String description) {
        super(name, date, venue);
        this.maxParticipants = maxParticipants;
        this.description = description;
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
