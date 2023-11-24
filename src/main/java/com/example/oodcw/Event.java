package com.example.oodcw;

import java.util.Date;
public class Event extends Schedule{
    private int maxParticipants;
    private String sponsors;
    private String details;
    private boolean mambersOnly;

    public Event(String name, Date date, String venue, int maxParticipants, String sponsors, String details, boolean mambersOnly) {
        super(name, date, venue);
        this.maxParticipants = maxParticipants;
        this.sponsors = sponsors;
        this.details = details;
        this.mambersOnly = mambersOnly;
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

    public boolean isMambersOnly() {
        return mambersOnly;
    }

    public void setMambersOnly(boolean mambersOnly) {
        this.mambersOnly = mambersOnly;
    }
}
