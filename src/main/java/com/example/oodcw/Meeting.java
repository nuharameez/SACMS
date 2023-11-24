package com.example.oodcw;

import java.util.Date;

public class Meeting extends Schedule{
    private String description;

    public Meeting(String name, Date date, String venue, String description) {
        super(name, date, venue);
        this.description=description;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Date getDate() {
        return super.getDate();
    }

    @Override
    public String getVenue() {
        return super.getVenue();
    }
}
