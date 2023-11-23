package com.example.oodcw;

import java.util.Date;

public abstract class Schedule {
    private String name;
    private Date date;
    private String venue;

    public Schedule(String name, Date date, String venue) {
        this.name = name;
        this.date = date;
        this.venue = venue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
