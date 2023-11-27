package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class ActivityController {

    @FXML
    private TextField activityName;

    @FXML
    private DatePicker activityDate;

    @FXML
    private TextField activityVenue;

    @FXML
    private TextField activityMaxParticipants;

    @FXML
    private TextArea activityDescription;

    @FXML
    private Button activitySubmitButton;

    @FXML
    private Button returnToView;

    @FXML
    private TextField activityID;

    @FXML
    private void initialize() {
        // Add listeners to check if all required fields are filled
        activityName.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityDate.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityVenue.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityMaxParticipants.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityDescription.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityID.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }

    private boolean checkFields() {
        // Enable the submit button only if all required fields are filled
        if (!activityName.getText().isEmpty() && activityDate.getValue() != null &&
                !activityVenue.getText().isEmpty() && !activityMaxParticipants.getText().isEmpty() &&
                !activityDescription.getText().isEmpty() && !activityID.getText().isEmpty())
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

    private void openFXML(String fxmlFileName, String stageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root));
        stage.show();
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
    @FXML
    private void handleReturnToView(ActionEvent event) throws IOException {
        openFXML("Schedule.fxml", "Add Meeting");
    }

    @FXML
    private void handleActivitySubmit() {
        try {
            if (!checkFields()) {
                showAlert("Please fill out all required fields.");
                return;
            }

            // Get values from the fields
            int ID;
            try {
                ID = Integer.parseInt(activityID.getText());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for Activity ID.");
                return;
            }

            // Check for duplicate meeting ID
            try {
                if (IDExists(ID)) {
                    throw new DuplicateIDException("Activity ID already exists. Please enter a different ID.");
                }
            } catch (DuplicateIDException e) {
                showAlert(e.getMessage());
                return;
            }
            String name = activityName.getText();
            LocalDate date = activityDate.getValue();

            // Check for existing schedules on the selected date
            if (isDateAlreadyScheduled(date)) {
                showAlert("There is already something scheduled on the selected date. Please choose a different date.");
                return;
            }

            String venue = activityVenue.getText();
            int participants = Integer.parseInt(activityMaxParticipants.getText());
            String description = activityDescription.getText();

            // Create an Activity object and save to the database
            Activity activity = new Activity(ID, name, date, venue, participants, description);
            activity.ActivitysaveToDatabase();
            showAlert("Activity Scheduled");
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for Max Participants or Activity ID.");
        } catch (Exception e) {
            showAlert("An error occurred. Please check your input values");
            e.printStackTrace();
        }
    }
}
