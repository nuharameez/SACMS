package com.example.oodcw;
import java.time.LocalDate;

public class Event extends Schedule{
    private int maxParticipants;
    private String sponsors;
    private String details;
    private String type;
    private boolean membersOnly;

    public Event(int ScheduleID, String name, LocalDate date, String venue, int maxParticipants, String sponsors, String details, boolean membersOnly, String club) {
        super( ScheduleID, name, date, venue, club);
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

}
