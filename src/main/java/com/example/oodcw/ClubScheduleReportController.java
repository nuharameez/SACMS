package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClubScheduleReportController {
    private SacmsDatabaseConnector databaseConnector;

    @FXML
    private ChoiceBox<String> club;

    @FXML
    private TableView<Meeting> meetingTable;

    @FXML
    private TableColumn<Meeting, Integer> meetingID;

    @FXML
    private TableColumn<Meeting, String> meetingName;

    @FXML
    private TableColumn<Meeting, String> meetingVenue;

    @FXML
    private TableColumn<Meeting, String> meetingDate;

    @FXML
    private TableColumn<Meeting, String> meetingDescription;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Integer> eventID;

    @FXML
    private TableColumn<Event, String> eventName;

    @FXML
    private TableColumn<Event, String> eventVenue;

    @FXML
    private TableColumn<Event, String> eventDate;

    @FXML
    private TableColumn<Event, Integer> eventMax;

    @FXML
    private TableColumn<Event, String> eventSponsors;

    @FXML
    private TableColumn<Event, String> eventMemOnly;

    @FXML
    private TableColumn<Event, String> eventDetails;

    @FXML
    private TableView<Activity> activityTable;

    @FXML
    private TableColumn<Activity, Integer> activityID;

    @FXML
    private TableColumn<Activity, String> activityName;

    @FXML
    private TableColumn<Activity, String> activityVenue;

    @FXML
    private TableColumn<Activity, String> activityDate;

    @FXML
    private TableColumn<Activity, Integer> activityMax;

    @FXML
    private TableColumn<Activity, String> activityDescription;

    @FXML
    private void initialize() {
        // Initialize database connector
        this.databaseConnector = new SacmsDatabaseConnector();

        try {
            List<String> clubNames = databaseConnector.getClubNames();
            ObservableList<String> clubList = FXCollections.observableArrayList(clubNames);
            club.setItems(clubList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        initMeetingTable();
        initEventTable();
        initActivityTable();
        // Add a listener to the ChoiceBox for selection changes
        club.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Load data for the selected club
                try (Connection connection = databaseConnector.dbConnector()) {
                    loadMeetingData(newValue, connection);
                    loadEventData(newValue, connection);
                    loadActivityData(newValue, connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle SQLException appropriately
                }
            } else {
                // Clear the tables if no club is selected
                clearTables();
            }
        });
    }

    private void clearTables() {
        meetingTable.getItems().clear();
        eventTable.getItems().clear();
        activityTable.getItems().clear();
    }

    private void initMeetingTable() {
        Connection connection = databaseConnector.dbConnector();
        meetingID.setCellValueFactory(new PropertyValueFactory<>("id"));
        meetingName.setCellValueFactory(new PropertyValueFactory<>("name"));
        meetingVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        meetingDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        meetingDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadMeetingData(club.getValue(), connection);
    }

    private void initEventTable() {
        Connection connection = databaseConnector.dbConnector();
        eventID.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        eventDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventMax.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        eventSponsors.setCellValueFactory(new PropertyValueFactory<>("sponsors"));
        eventMemOnly.setCellValueFactory(new PropertyValueFactory<>("memberOnly"));
        eventDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
        loadEventData(club.getValue(),connection);
    }

    private void initActivityTable() {
        Connection connection = databaseConnector.dbConnector();
        activityID.setCellValueFactory(new PropertyValueFactory<>("id"));
        activityName.setCellValueFactory(new PropertyValueFactory<>("name"));
        activityVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        activityDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        activityMax.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        activityDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadActivityData(club.getValue(),connection);
    }
    private void loadMeetingData(String clubName, Connection connection) {
        try {
            List<Meeting> meetings = databaseConnector.getMeetingsByClub(clubName, connection);
            meetingTable.setItems(FXCollections.observableArrayList(meetings));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEventData(String clubName, Connection connection) {
        try {
            List<Event> events = databaseConnector.getEventsForClub(clubName, connection);
            eventTable.setItems(FXCollections.observableArrayList(events));
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }
    }

    private void loadActivityData(String clubName, Connection connection) {
        try {
            List<Activity> activities = databaseConnector.getActivitiesForClub(clubName, connection);
            activityTable.setItems(FXCollections.observableArrayList(activities));
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }
    }

}
