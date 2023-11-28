package com.example.oodcw;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MeetingController {
    @FXML
    private TextField meetingName;

    @FXML
    private DatePicker meetingDate;

    @FXML
    private TextField meetingVenue;

    @FXML
    private TextArea meetingDescription;

    @FXML
    private Button meetingSubmitButton;

    @FXML
    private Button returnToView;
    @FXML
    private TextField meetingID;

    @FXML
    private void initialize() {
        // Add listeners to check if all required fields are filled
        meetingName.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        meetingDate.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
        meetingVenue.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        meetingID.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }

    private boolean checkFields() {
        // Enable the submit button only if all required fields are filled
        if(!meetingName.getText().isEmpty() && meetingDate.getValue() != null && !meetingVenue.getText().isEmpty())
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
    @FXML
    private void handleReturnToView (ActionEvent event) throws IOException {
        openFXML("Schedule.fxml", "Schedule App");
    }

    @FXML
    private void handleMeetingSubmit() {
        try {
            if (!checkFields()) {
                showAlert("Please fill out all required fields.");
                return;
            }

            int ID;
            try {
                ID = Integer.parseInt(meetingID.getText());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for Meeting ID.");
                return;
            }
            String name = meetingName.getText();
            String venue = meetingVenue.getText();
            String description = meetingDescription.getText();
            LocalDate date = meetingDate.getValue();
            Meeting meeting = new Meeting(ID,name,date,venue,description);
            try {
                if (meeting.IDExists(ID)) {
                    throw new DuplicateIDException("Meeting ID already exists. Please enter a different ID.");
                }
            } catch (DuplicateIDException e) {
                showAlert(e.getMessage());
                return;
            }
            if (meeting.isDateAlreadyScheduled(date)) {
                showAlert("There is already something scheduled on the selected date. Please choose a different date.");
                return;
            }
            meeting.saveToDatabase();
            showAlert("Meeting Scheduled");
        } catch (Exception e) {
            showAlert("An error occurred. Please check your input values");
            e.printStackTrace();
        }
    }
}
