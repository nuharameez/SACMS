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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
                !eventVenue.getText().isEmpty() && !maxParticipants.getText().isEmpty() && !eventID.getText().isEmpty()&& !MemOnly.getValue().isEmpty())
            return true;
        else
            return false;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
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
        openFXML("Schedule.fxml", "Schedule");
    }

    @FXML
    private void handleEventSubmit(){
            if (!checkFields()) {
               showAlert("Please fill out all required fields.");
                return;
            }
            int ID;

            try {
                ID = Integer.parseInt(eventID.getText());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for Event ID.");
                return;
            }

            String name = nameEvent.getText();
            LocalDate date=eventDate.getValue();

            String venue = eventVenue.getText();
            int participants;
            try {
                participants = Integer.parseInt(maxParticipants.getText());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for Max Participants.");
                return;
            }
            String sponsorDetails = sponsors.getText();
            String details = eventDetails.getText();
            String memberOnlyString = MemOnly.getValue();
            boolean isMemberOnly = "Yes".equals(memberOnlyString);
            Event event = new Event(ID, name, date, venue, participants, sponsorDetails, details, isMemberOnly);
            try {
                if (event.checkID(ID)) {
                    throw new DuplicateIDException("Event ID already exists. Please enter a different ID.");
                }
            } catch (DuplicateIDException e) {
                showAlert(e.getMessage());
                return;
            }
            if (event.checkDate(date)) {
                showAlert("There is already something scheduled on the selected date. Please choose a different date.");
                return;
            }
            event.saveToDatabase();
            showAlert("Event Scheduled");
        }
    }

