package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClubAdvisorMenu {

    @FXML
    private void handleCreateClubs(ActionEvent event) throws IOException {
        openFXML("clubCreation.fxml", "Create Clubs");
    }

    @FXML
    private void handleManageClubs(ActionEvent event) throws IOException {
        openFXML("manageClubs.fxml", "Manage Clubs");
    }

    @FXML
    private void handleSchedule(ActionEvent event) throws IOException {
        openFXML("Schedule.fxml", "Schedule");
        System.out.println("Schedule button clicked");
    }

    @FXML
    private void handleTrackAttendance(ActionEvent event) throws IOException {
        openFXML("TrackAttendance.fxml", "Track Attendance");
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
