package com.example.oodcw;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SacmsDatabaseConnector implements DatabaseConnector {

    public Connection dbConnector() {
        try {
            return DriverManager.getConnection(
                    /*"jdbc:mysql://localhost:3306/sacms",
                    "root",
                    "z518@gh34abde158"*/
                    "jdbc:mysql://127.0.0.1:3306/scams_schedule",
                    "root",
                    "1234"
            );
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return null;
    }

    public List<Event> getEventsForClub(String clubName, Connection connection) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM schedule JOIN event ON schedule.ScheduleID = event.ScheduleID WHERE schedule.Club = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, clubName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Event event = new Event(resultSet);
                    events.add(event);
                }
            }
        }
        return events;
    }

    // Method to get activities for a specific club
    public List<Activity> getActivitiesForClub(String clubName, Connection connection) throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM schedule JOIN activity ON schedule.ScheduleID = activity.ScheduleID WHERE schedule.Club = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, clubName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Activity activity = new Activity(resultSet);
                    activities.add(activity);
                }
            }
        }
        return activities;
    }


    public static List<String> getClubNames(Connection connection) throws SQLException {
        List<String> clubs = new ArrayList<>();
        String query = "SELECT clubname FROM clubstable";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                clubs.add(resultSet.getString("clubname"));
            }
        }

        return clubs;
    }

    public List<Meeting> getMeetingsByClub(String clubName, Connection connection) throws SQLException {
        List<Meeting> meetings = new ArrayList<>();
        String query = "SELECT * FROM schedule JOIN meeting ON schedule.ScheduleID = meeting.ScheduleID WHERE Club = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, clubName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Meeting meeting = new Meeting(resultSet);
                    meetings.add(meeting);
                }
            }
        }

        return meetings;
    }

    public List<String> getClubNames() throws SQLException {
        List<String> clubs = new ArrayList<>();
        String query = "SELECT clubname FROM clubstable";

        try (Connection connection = dbConnector();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                clubs.add(resultSet.getString("clubname"));
            }
        }

        return clubs;
    }

    public static List<Schedule> getDataFromDatabase(Connection connection)  {
        List<Schedule> scheduleList = new ArrayList<>();
        try {
        String query = "SELECT s.ScheduleID, s.Name, s.Venue, s.Date, s.Type, s.Club," +
                "m.Description AS MeetingDescription, " +
                "e.Sponsors, e.Details, e.MemberOnly, e.MaxParticipants AS EventMaxParticipants, " +
                "a.MaxParticipants AS ActivityMaxParticipants, a.Description AS ActivityDescription " +
                "FROM schedule s " +
                "LEFT JOIN meeting m ON s.ScheduleID = m.ScheduleID " +
                "LEFT JOIN event e ON s.ScheduleID = e.ScheduleID " +
                "LEFT JOIN activity a ON s.ScheduleID = a.ScheduleID";
        PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int scheduleID = resultSet.getInt("ScheduleID");
                String name = resultSet.getString("Name");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                String venue = resultSet.getString("Venue");
                String type = resultSet.getString("Type");
                String club = resultSet.getString("Club");

                Schedule schedule;

                switch (type) {
                    case "Meeting":
                        String meetingDescription = resultSet.getString("MeetingDescription");
                        schedule = new Meeting(scheduleID, name, date, venue, meetingDescription,club);
                        break;
                    case "Event":
                        String sponsors = resultSet.getString("Sponsors");
                        String details = resultSet.getString("Details");
                        boolean memberOnly = "Yes".equals(resultSet.getString("MemberOnly"));
                        int eventMaxParticipants = resultSet.getInt("EventMaxParticipants");
                        schedule = new Event(scheduleID, name, date, venue, eventMaxParticipants, sponsors, details, memberOnly, club);
                        break;
                    case "Activity":
                        int activityMaxParticipants = resultSet.getInt("ActivityMaxParticipants");
                        String activityDescription = resultSet.getString("ActivityDescription");
                        schedule = new Activity(scheduleID, name, date, venue, activityMaxParticipants, activityDescription, club);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown schedule type: " + type);
                }

                scheduleList.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return scheduleList;

    }

    public static void saveScheduleToDatabase(Schedule schedule, Connection connection) {
            String scheduleQuery = "INSERT INTO schedule (ScheduleID, Name, Date, Venue, Type,Club) VALUES (?, ?, ?, ?, ?,?)";
            try (PreparedStatement scheduleStatement = connection.prepareStatement(scheduleQuery)) {
                scheduleStatement.setInt(1, schedule.getScheduleID());
                scheduleStatement.setString(2, schedule.getName());
                scheduleStatement.setObject(3, schedule.getDate());
                scheduleStatement.setString(4, schedule.getVenue());
                scheduleStatement.setString(5, schedule.getType());
                scheduleStatement.setString(6,schedule.getClub());

                scheduleStatement.executeUpdate();

                if (schedule instanceof Meeting) {
                saveMeetingToDatabase((Meeting) schedule, connection);
                } else if (schedule instanceof Event) {
                    saveEventToDatabase((Event) schedule, connection);
                } else if (schedule instanceof Activity) {
                    saveActivityToDatabase((Activity) schedule, connection);
                }
            } catch (SQLException e) {
                System.out.println("Schedule table does not exist. Creating new table ... ");
                String createTableSQL = "CREATE TABLE " + "schedule" + " (ScheduleID int PK, Name varchar(45), Venue varchar(45), Date date, Type enum('Meeting','Activity','Event'),Club varchar(45))";
                try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                saveScheduleToDatabase(schedule, connection);
            }
        }

    private static void saveMeetingToDatabase(Meeting meeting, Connection connection)  {
        String meetingQuery = "INSERT INTO meeting (ScheduleID, Description) VALUES (?, ?)";
        try (PreparedStatement meetingStatement = connection.prepareStatement(meetingQuery)) {
            meetingStatement.setInt(1, meeting.getScheduleID());
            meetingStatement.setString(2, meeting.getDescription());

            meetingStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Meeting table does not exist. Creating new table ... ");
            String createTableSQL = "CREATE TABLE " + "meeting" + " (ScheduleID int PK, Description longtext)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            saveMeetingToDatabase(meeting, connection);
        }
    }

    private static void saveEventToDatabase(Event event, Connection connection)  {
        String eventQuery = "INSERT INTO event (ScheduleID, MaxParticipants, sponsors, details, MemberOnly) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement eventStatement = connection.prepareStatement(eventQuery)) {
            eventStatement.setInt(1, event.getScheduleID());
            eventStatement.setInt(2, event.getMaxParticipants());
            eventStatement.setString(3, event.getSponsors());
            eventStatement.setString(4, event.getDetails());
            eventStatement.setString(5, event.isMembersOnly() ? "Yes" : "No");

            eventStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Event table does not exist. Creating new table ... ");
            String createTableSQL = "CREATE TABLE " + "event" + " (ScheduleID int PK, Sponsors VARCHAR(60),Details varchar(45),MemberOnly enum('Yes','No'), MaxParticipants int)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            saveEventToDatabase(event, connection);
        }

    }

    private static void saveActivityToDatabase(Activity activity, Connection connection)  {
        String activityQuery = "INSERT INTO activity (ScheduleID, MaxParticipants, Description) VALUES (?, ?, ?)";
        try (PreparedStatement activityStatement = connection.prepareStatement(activityQuery)) {
            activityStatement.setInt(1, activity.getScheduleID());
            activityStatement.setInt(2, activity.getMaxParticipants());
            activityStatement.setString(3, activity.getDescription());

            activityStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Activity table does not exist. Creating new table ... ");
            String createTableSQL = "CREATE TABLE " + "activity" + " (ScheduleId int, MaxParticipants int, Description longtext)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            saveActivityToDatabase(activity, connection);
        }
        }

    public static boolean IDExists(int ID, Connection connection)  {
        String query = "SELECT COUNT(*) FROM schedule WHERE ScheduleID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ID);  // Set the parameter value for the placeholder
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean deleteSchedule(int ID, Connection connection)  {
        if (IDExists(ID, connection)) {
            String deleteQuery = "DELETE FROM schedule WHERE ScheduleID = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, ID);
                int affectedRows = deleteStatement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false; // Schedule with given ID does not exist
    }


    public static boolean isDateAlreadyScheduled(LocalDate date, Connection connection)  {
        try { String query = "SELECT COUNT(*) FROM schedule WHERE Date = ?";

        PreparedStatement statement = null;

            statement = connection.prepareStatement(query);

        statement.setObject(1, date);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static List<String> getAdvisorClubsFromDatabase(String name, Connection connection) throws SQLException {
        List<String> advisorClubs = new ArrayList<>();
        String userName = name;
        String query = "SELECT clubname FROM clubstable WHERE clubAdvisor = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, userName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String clubName = resultSet.getString("clubname");
                        advisorClubs.add(clubName);
                    }
                }
        return advisorClubs;
    }



    //this method used in register class will malfunction since it also checks if the password is the same,
    // we need to check only the id

    //authenticating the user for login

    //1.1.2.2 ----> login sequence
    public boolean authenticateUser(String role, String username, String password, Connection connection) {

        String sql = "SELECT * FROM " + role + " WHERE " + role + "Username = ? AND " + role + "Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");

        }
        return false;
    }

    //1.1.2.3 ----> login sequence
    public UserDetails getUserDetails(String role, String username, Connection connection) {
        String query = "SELECT " + role + "Id, " + role + "Name FROM " + role + " WHERE " + role+ "Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString(role + "Id");
                String name = resultSet.getString(role + "Name");
                return new UserDetails(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //checking if the id already exists.
    //if id exists the user already has an account.


    //checking if the id already exists.
    //if id exists the user already has an account.
    //check if username is unique

    //1.1.1& 1.1.3 ----> register sequence
    @Override
    public boolean authenticateRegistration(String role, String authenticator, String columnName, Connection connection) {

        String sql = "SELECT * FROM " + role + " WHERE " + role + columnName + " = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, authenticator);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");
        }
        return false;
    }


    //method to add new registry to the database

    //1.1.5 ----> register sequence
    public void addNewUser(String role, String id, String name, String username, String password, Connection connection) {
        String sql = "INSERT INTO " + role + " (" + role + "Id, " + role + "Name, " + role + "Username, " + role + "Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(role + "table does not exist in the system.");

            try (Statement statement = connection.createStatement()) {
                String createTableSQL = "CREATE TABLE " + role + " (" + role + "Id VARCHAR(45), " + role + "Name VARCHAR(50), " + role + "Username VARCHAR(45), " + role + "Password VARCHAR(45))";
                statement.executeUpdate(createTableSQL);

                // Retry the insert after creating the table
                addNewUser(role, id, name, username, password, connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    //to populate observable arraylist with the club options.

    public void clubOptions(ObservableList<String> clubOption) {
        try {
            Statement statement = dbConnector().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clubName FROM clubstable");
            while (resultSet.next()) {
                String clubName = resultSet.getString("clubName");
                clubOption.add(clubName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }

    }

    //1.1.1.1.4 ----> join club sequence
    //adding student id and name to the relevant club table when they want to join a particular club
    public void addStudentToClubTable(String id, String name, String clubName, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + clubName + "(studentId, studentName) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //creating table if club table not created
            //usually necessary when adding the first student to the database
            System.out.println("Club table does not exist. Creating new table ... ");
            String createTableSQL = "CREATE TABLE " + clubName + " (studentId VARCHAR(45), studentName VARCHAR(50))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            }
            addStudentToClubTable(id, name, clubName, connection);
        }
    }

    //1.1.1.1 ----> join club sequence
    public boolean authenticateJoinClub(String clubName, String id, Connection connection) {
        String sql = "SELECT * FROM " + clubName + " WHERE studentId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
        return false;
    }

    //1.1.2.1 & 1.1.2.2 ----> reporting sequence
    public String getClubAdvisorName(String clubName, Connection connection) {
        String sql = "SELECT clubAdvisor FROM clubstable WHERE  clubName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clubName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("clubAdvisor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //1.1.2.3 & 1.1.2.4 ----> reporting sequence
    public int getStudentCount(String clubTable, Connection connection) {
        String sql = "SELECT COUNT(*) AS studentCount FROM " + clubTable;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("studentCount");
            }
        } catch (SQLException e) {
            System.out.println("The club table does not exist");
            ;
        }
        return 0;
    }

    //1.1.2.5 & 1.1.2.6 ----> reporting sequence
    public ObservableList<Student> getStudents(String clubTable, Connection connection) {
        ObservableList<Student> studentData = FXCollections.observableArrayList();
        String sql = "SELECT studentId, studentName FROM " + clubTable;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String studentId = resultSet.getString("studentId");
                String studentName = resultSet.getString("studentName");
                studentData.add(new Student(studentId, studentName));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return studentData;
    }






    public void createClub(String Name, String Category, String Advisor, String Motto, Connection connection) throws SQLException {
        // if club is not in database, create and insert
        String createClubSqlQuery = "INSERT INTO clubsTable (clubName, clubCategory, clubAdvisor, clubMotto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createClubSqlQuery)) {
            preparedStatement.setString(1, Name);
            preparedStatement.setString(2, Category);
            preparedStatement.setString(3, Advisor);
            preparedStatement.setString(4, Motto);
            // Execute query to create new club
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            String sql = "CREATE TABLE IF NOT EXISTS " + "clubstable" + " (" +
                    "clubID INT AUTO_INCREMENT PRIMARY KEY," +
                    "clubName VARCHAR(50)," +
                    "clubCategory VARCHAR(50)," +
                    "clubAdvisor VARCHAR(50)," +
                    "clubMotto VARCHAR(200)" + ")";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
            createClubSqlQuery = "INSERT INTO clubsTable (clubName, clubCategory, clubAdvisor, clubMotto) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createClubSqlQuery)) {
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, Category);
                preparedStatement.setString(3, Advisor);
                preparedStatement.setString(4, Motto);
                // Execute query to create new club
                preparedStatement.executeUpdate();

            }
        }
    }


    public void deleteClub(String Name, Connection connection) throws SQLException {
        String deleteClubSqlQuery = "DELETE FROM clubTable WHERE clubname = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteClubSqlQuery)) {
            preparedStatement.setString(1, Name);

            //execute the delete query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void editClub(String newClubName, String clubCategory, String clubAdvisor, String clubMotto, int clubID, Connection connection) throws SQLException {
        // create and insert
        String updateQuery = "UPDATE clubstable SET " +
                "clubName=?, " +
                "clubCategory=?, " +
                "clubAdvisor=?, " +
                "clubMotto=? " +
                "WHERE clubId=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newClubName);
            preparedStatement.setString(2, clubCategory);
            preparedStatement.setString(3, clubAdvisor);
            preparedStatement.setString(4, clubMotto);
            preparedStatement.setInt(5, clubID);

            // Execute the update statement
            preparedStatement.executeUpdate();
            System.out.println("Club updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't update club!");
        }

    }

    public  UserDetails getUserDetails1(String username, Connection connection) {
        String query = "SELECT clubadvisorId, clubadvisorName FROM clubadvisor WHERE  clubadvisorUsername = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString("clubadvisorId");
                String name = resultSet.getString("clubadvisorName");
                return new UserDetails(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean clubExists(String clubName, Connection connection) {

        // Check if the club exists
        String checkClubExistsQuery = "SELECT * FROM clubstable WHERE clubName = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkClubExistsQuery)) {
            checkStatement.setString(1, clubName);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                return resultSet.next(); // if the club name exists returns true if not false
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

}