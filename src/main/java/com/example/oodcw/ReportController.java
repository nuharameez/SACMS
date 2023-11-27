package com.example.oodcw;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportController {
    @FXML
    private void onClubMembershipClick() {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("clubMembershipReport.fxml"));
        } catch (IOException e) {
            System.out.println("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    private void onEventAttendanceClick() {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("eventAttendanceReport.fxml"));
        } catch (IOException e) {
            System.out.println("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    private void onClubActivitiesClick() {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("clubActitvitiesReport.fxml"));
        } catch (IOException e) {
            System.out.println("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

}
