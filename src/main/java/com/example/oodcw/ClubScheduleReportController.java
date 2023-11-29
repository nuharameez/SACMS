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
        this.databaseConnector = new SacmsDatabaseConnector();
        Connection connection=databaseConnector.dbConnector();

        try {
            List<String> clubNames = SacmsDatabaseConnector.getClubNames(databaseConnector.dbConnector());
            ObservableList<String> clubList = FXCollections.observableArrayList(clubNames);
            club.setItems(clubList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add a listener to the ChoiceBox for selection changes
        club.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Load data for the selected club
                List<Meeting> meetings = null;
                try {
                    meetings = databaseConnector.getMeetingsByClub(newValue, connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                meetingTable.setItems(FXCollections.observableArrayList(meetings));
                List<Event> events = null;
                try {
                    events = databaseConnector.getEventsForClub(newValue, connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                eventTable.setItems(FXCollections.observableArrayList(events));
                List<Activity> activities = null;
                try {
                    activities = databaseConnector.getActivitiesForClub(newValue, connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                activityTable.setItems(FXCollections.observableArrayList(activities));

                    // Handle SQLException appropriately
                }
             else {
                // Clear the tables if no club is selected
                clearTables();
            }
        });

        initMeetingTable();
        initEventTable();
        initActivityTable();
    }

    private void clearTables() {
        meetingTable.getItems().clear();
        eventTable.getItems().clear();
        activityTable.getItems().clear();
    }

    private void initMeetingTable() {
        meetingName.setCellValueFactory(new PropertyValueFactory<>("name"));
        meetingVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        meetingDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        meetingDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

    }

    private void initEventTable() {
        eventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        eventDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventMax.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        eventSponsors.setCellValueFactory(new PropertyValueFactory<>("sponsors"));
        eventMemOnly.setCellValueFactory(new PropertyValueFactory<>("memberOnly"));
        eventDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
    }

    private void initActivityTable() {
        activityName.setCellValueFactory(new PropertyValueFactory<>("name"));
        activityVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        activityDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        activityMax.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        activityDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

}
