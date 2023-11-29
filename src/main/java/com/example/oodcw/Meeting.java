package com.example.oodcw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Meeting extends Schedule{
    private String description;
    private String type;

    public Meeting(int ScheduleID, String name, LocalDate date, String venue, String description, String club) {
        super(ScheduleID,name, date, venue, club);
        this.description=description;
        this.type="Meeting";
    }

    public Meeting(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.description = resultSet.getString("Description");
    }

    public String getDescription() {

        return description;
    }
    @Override
    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
