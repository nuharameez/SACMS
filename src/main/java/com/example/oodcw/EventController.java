package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class EventController {

    @FXML
    private TextField nameEvent;

    @FXML
    private DatePicker eventDate;

    @FXML
    private TextField eventVenue;

    @FXML
    private TextField maxParticipants;

    @FXML
    private TextField sponsors;

    @FXML
    private TextArea eventDetails;

    @FXML
    private ChoiceBox<String> MemOnly;

    @FXML
    private Button eventSubmitButton;

    @FXML
    private Button returnToView;

    @FXML
    private TextField eventID;

    @FXML
    private void initialize() {
        // Add listeners to check if all required fields are filled
        nameEvent.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        eventDate.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
        eventVenue.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        maxParticipants.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        eventID.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        MemOnly.getItems().addAll("Yes", "No");
    }

    private boolean checkFields() {
        // Enable the submit button only if all required fields are filled
        if (!nameEvent.getText().isEmpty() && eventDate.getValue() != null &&
                !eventVenue.getText().isEmpty() && !maxParticipants.getText().isEmpty() && !eventID.getText().isEmpty())
            return true;
        else
            return false;
    }

    private void showAlert(String message) {
        // Display an alert with the given message
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isDateAlreadyScheduled(LocalDate date) {
        try (Connection connection = DatabaseController.getConnection()) {
            String query = "SELECT COUNT(*) FROM schedule WHERE Date = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, date);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean IDExists(int meetingID) {
        try (Connection connection = DatabaseController.getConnection()) {
            String query = "SELECT COUNT(*) FROM schedule WHERE ScheduleID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, meetingID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openFXML(String fxmlFileName, String stageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleReturnToView(ActionEvent event) throws IOException {
        openFXML("Schedule.fxml", "Add Meeting");
    }

    @FXML
    private void handleEventSubmit() throws SQLException {
            if (!checkFields()) {
               showAlert("Please fill out all required fields.");
                return;
            }
            // Get values from the fields
            int ID;
            try {
                ID = Integer.parseInt(eventID.getText());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for Event ID.");
                return;
            }

            // Check for duplicate meeting ID
            try {
                if (IDExists(ID)) {
                    throw new DuplicateMeetingIDException("Event ID already exists. Please enter a different ID.");
                }
            } catch (DuplicateMeetingIDException e) {
                showAlert(e.getMessage());
                return;
            }

            String name = nameEvent.getText();

            LocalDate date = eventDate.getValue();

            // Check for existing schedules on the selected date
            if (isDateAlreadyScheduled(date)) {
                showAlert("There is already something scheduled on the selected date. Please choose a different date.");
                return;
            }

            String venue = eventVenue.getText();
            int participants = Integer.parseInt(maxParticipants.getText());
            String sponsorDetails = sponsors.getText();
            String details = eventDetails.getText();
            String memberOnlyString = MemOnly.getValue();
            boolean isMemberOnly = "Yes".equals(memberOnlyString);

            Event event = new Event(ID, name, date, venue, participants, sponsorDetails, details, isMemberOnly);
            event.EventsaveToDatabase();
            showAlert("Event Scheduled");

        }
    }

