package com.example.oodcw;

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
}
