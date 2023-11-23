package com.example.oodcw;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Schedule{

    @FXML
    private AnchorPane schedulePane;

    @FXML
    private Text pageTitle;

    // Fields for Meeting page
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

    // Fields for Event page
    @FXML
    private TextField eventName;

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
    private ChoiceBox<String> memOnly;

    @FXML
    private Button eventSubmitButton;

    // Fields for Activity page
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

    // Fields for Schedule page
    @FXML
    private TableView<String> tableView;


    @FXML
    private TableColumn<String, String> dateColumn;

    @FXML
    private TableColumn<String, String> typeColumn;

    @FXML
    private TableColumn<String, String> nameColumn;
    @FXML
    private Button addEventButton;

    @FXML
    private Button addMeetingButton;

    @FXML
    private Button addActivityButton;

    // You can add more methods and logic here.

    // For example, you might want to initialize the table and buttons.
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    // You can define methods for button actions or other events
    @FXML
    private void handleAddEvent() {
        switchToPage("Event.fxml", "Schedule Event");
    }

    @FXML
    private void handleAddMeeting() {
        switchToPage("Meeting.fxml", "Schedule Meeting");
    }

    @FXML
    private void handleAddActivity() {
        switchToPage("Activity.fxml", "Schedule Activity");
    }

    @FXML
    private void handleSubmitMeeting() {
        showAlert("Meeting submitted!");
    }

    @FXML
    private void handleSubmitEvent() {
        showAlert("Event submitted!");
    }

    @FXML
    private void handleSubmitActivity() {
        showAlert("Activity submitted!");
    }

    private void switchToPage(String fxmlFileName, String title) {
        // Add logic to switch the view to the specified page
        // For example, you can set the visibility of panes based on the selected page.
        try {
            AnchorPane newPage = FXMLLoader.load(getClass().getResource(fxmlFileName));
            schedulePane.getChildren().setAll(newPage);
            pageTitle.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Submission Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
