package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScheduleController {

    @FXML
    private void handleAddEvent(ActionEvent event) {
        openFXML("Event.fxml", "Add Event");
    }

    @FXML
    private void handleAddMeeting(ActionEvent event) {
        openFXML("Meeting.fxml", "Add Meeting");
    }

    @FXML
    private void handleAddActivity(ActionEvent event) {
        openFXML("Activity.fxml", "Add Activity");
    }

    private void openFXML(String fxmlFileName, String stageTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(stageTitle);
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
