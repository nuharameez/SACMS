package com.example.oodcw;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/scams_schedule";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static List<Schedule> getDataFromDatabase() throws SQLException {
        List<Schedule> scheduleList = new ArrayList<>();
        String query = "SELECT s.ScheduleID, s.Name, s.Venue, s.Date, s.Type, " +
                "m.Description AS MeetingDescription, " +
                "e.Sponsors, e.Details, e.MemberOnly, e.MaxParticipants AS EventMaxParticipants, " +
                "a.MaxParticipants AS ActivityMaxParticipants, a.Description AS ActivityDescription " +
                "FROM schedule s " +
                "LEFT JOIN meeting m ON s.ScheduleID = m.ScheduleID " +
                "LEFT JOIN event e ON s.ScheduleID = e.ScheduleID " +
                "LEFT JOIN activity a ON s.ScheduleID = a.ScheduleID";

        try (Connection connection = DatabaseController.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int scheduleID = resultSet.getInt("ScheduleID");
                String name = resultSet.getString("Name");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String venue = resultSet.getString("Venue");
                String type = resultSet.getString("Type");

                Schedule schedule;

                switch (type) {
                    case "Meeting":
                        String meetingDescription = resultSet.getString("MeetingDescription");
                        schedule = new Meeting(scheduleID, name, date, venue, meetingDescription);
                        break;
                    case "Event":
                        String sponsors = resultSet.getString("Sponsors");
                        String details = resultSet.getString("Details");
                        boolean memberOnly = "Yes".equals(resultSet.getString("MemberOnly"));
                        int eventMaxParticipants = resultSet.getInt("EventMaxParticipants");
                        schedule = new Event(scheduleID, name, date, venue, eventMaxParticipants, sponsors, details, memberOnly);
                        break;
                    case "Activity":
                        int activityMaxParticipants = resultSet.getInt("ActivityMaxParticipants");
                        String activityDescription = resultSet.getString("ActivityDescription");
                        schedule = new Activity(scheduleID, name, date, venue, activityMaxParticipants, activityDescription);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown schedule type: " + type);
                }

                scheduleList.add(schedule);
            }
        }

        return scheduleList;
    }
    public static void saveScheduleToDatabase(Schedule schedule) {
        try (Connection connection = DatabaseController.getConnection()) {
            String scheduleQuery = "INSERT INTO schedule (ScheduleID, Name, Date, Venue, Type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement scheduleStatement = connection.prepareStatement(scheduleQuery)) {
                scheduleStatement.setInt(1, schedule.getScheduleID());
                scheduleStatement.setString(2, schedule.getName());
                scheduleStatement.setObject(3, schedule.getDate());
                scheduleStatement.setString(4, schedule.getVenue());
                scheduleStatement.setString(5, schedule.getType());

                scheduleStatement.executeUpdate();
            }

            // Insert into other tables based on the schedule type
            if (schedule instanceof Meeting) {
                saveMeetingToDatabase((Meeting) schedule, connection);
            } else if (schedule instanceof Event) {
                saveEventToDatabase((Event) schedule, connection);
            } else if (schedule instanceof Activity) {
                saveActivityToDatabase((Activity) schedule, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveMeetingToDatabase(Meeting meeting, Connection connection) throws SQLException {
        String meetingQuery = "INSERT INTO meeting (ScheduleID, Description) VALUES (?, ?)";
        try (PreparedStatement meetingStatement = connection.prepareStatement(meetingQuery)) {
            meetingStatement.setInt(1, meeting.getScheduleID());
            meetingStatement.setString(2, meeting.getDescription());

            meetingStatement.executeUpdate();
        }
    }

    private static void saveEventToDatabase(Event event, Connection connection) throws SQLException {


    }

    private static void saveActivityToDatabase(Activity activity, Connection connection) throws SQLException {
        String activityQuery = "INSERT INTO activity (ScheduleID, MaxParticipants, Description) VALUES (?, ?, ?)";
        try( PreparedStatement activityStatement = (connection.prepareStatement(activityQuery))){
            activityStatement.setInt(1, activity.getScheduleID());
            activityStatement.setInt(2, activity.getMaxParticipants());
            activityStatement.setString(3, activity.getDescription());

            activityStatement.executeUpdate();
        }
    }
}

