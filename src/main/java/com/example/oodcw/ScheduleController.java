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
    private void handleAddEvent(ActionEvent event) throws IOException {
        openFXML("Event.fxml", "Add Event");
        System.out.println("hi");
    }

    @FXML
    private void handleAddMeeting(ActionEvent event) throws IOException {
        openFXML("Meeting.fxml", "Add Meeting");
    }

    @FXML
    private void handleAddActivity(ActionEvent event) throws IOException {
        openFXML("Activity.fxml", "Add Activity");
    }

    private void openFXML(String fxmlFileName, String stageTitle) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root));
        stage.show();
    }
}

