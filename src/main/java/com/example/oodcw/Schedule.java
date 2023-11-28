package com.example.oodcw;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class Schedule {
    private int ScheduleID;
    private String name;
    private LocalDate date;
    private String venue;

    private String type;

    private String club;


    private SacmsDatabaseConnector databaseConnector;


    public Schedule() {
    }

    public Schedule(int ScheduleID,String name, LocalDate date, String venue, String club) {
        this.ScheduleID=ScheduleID;
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.club=club;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
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
    public boolean checkDate(LocalDate date) throws SQLException {
        Connection connection = databaseConnector.dbConnector();
        SacmsDatabaseConnector.isDateAlreadyScheduled(date, connection);
        return false;
    }

    public boolean checkID (int ID) throws SQLException {
        Connection connection = databaseConnector.dbConnector();

        SacmsDatabaseConnector.IDExists(ID, connection);
        return false;
    }
    public void saveToDatabase() throws SQLException {
        Connection connection = databaseConnector.dbConnector();
        SacmsDatabaseConnector.saveScheduleToDatabase(this, connection);
    }
}
