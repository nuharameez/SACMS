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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


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
    private ChoiceBox<String> activityClub;

    @FXML
    private TextField activityID;


    @FXML
    private void initialize() throws SQLException {
        // Add listeners to check if all required fields are filled
        activityName.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityDate.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityVenue.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityMaxParticipants.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityDescription.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        activityID.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        loadAdvisorClubs();
    }
    private SacmsDatabaseConnector databaseConnector;


    // Setter method for loggedInUser

    private void loadAdvisorClubs() throws SQLException {
        Connection connection = databaseConnector.dbConnector();
        List<String> advisorClubs = SacmsDatabaseConnector.getAdvisorClubsFromDatabase(connection);
        activityClub.getItems().addAll(advisorClubs);
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

            String name = activityName.getText();
            LocalDate date= activityDate.getValue();
            String venue = activityVenue.getText();
            String club = activityClub.getValue();
            int participants;
            try {
                participants = Integer.parseInt(activityMaxParticipants.getText());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for Max Participants.");
                return;
            }
            String description = activityDescription.getText();
            Activity activity = new Activity(ID, name, date, venue, participants, description,club);
            try {
                if (activity.checkID(ID)) {
                    throw new DuplicateIDException("Activity ID already exists. Please enter a different ID.");
                }
            } catch (DuplicateIDException e) {
                showAlert(e.getMessage());
                return;
            }
            if (activity.checkDate(date)) {
                showAlert("There is already something scheduled on the selected date. Please choose a different date.");
                return;
            }

            activity.saveToDatabase();
            showAlert("Activity Scheduled");
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for Max Participants or Activity ID.");
        } catch (Exception e) {
            showAlert("An error occurred. Please check your input values");
            e.printStackTrace();
        }
    }
}
