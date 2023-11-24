package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private void handleEventSubmitButton(ActionEvent event) {

        String eventName = nameEvent.getText();
        String venue = eventVenue.getText();
        // Get other values similarly

        // Implement your logic here

        // For example, you can print the values to the console
        System.out.println("Event Name: " + eventName);
        System.out.println("Venue: " + venue);
        // Print other values similarly
    }
}
