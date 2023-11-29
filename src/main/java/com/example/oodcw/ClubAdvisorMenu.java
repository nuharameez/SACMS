package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClubAdvisorMenu {

    @FXML
    private void handleCreateClubs(ActionEvent event) throws IOException {
        openFXML("clubCreation.fxml", "Create Clubs");
        Stage previousStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        previousStage.close();

    }

    @FXML
    private void handleManageClubs(ActionEvent event) throws IOException {
        openFXML("manageClubs.fxml", "Manage Clubs");
        Stage previousStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        previousStage.close();
    }

    @FXML
    private void handleSchedule(ActionEvent event) throws IOException {
        openFXML("Schedule.fxml", "Schedule");
        Stage previousStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        previousStage.close();
        System.out.println("Schedule button clicked");
    }

    //1 & 1.1 ----> reporting sequence
    @FXML
    private void onViewReportsClick(ActionEvent actionEvent) {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("viewReports.fxml"));
        } catch (IOException e) {
            System.out.println("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
        Stage previousStage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        previousStage.close();
    }

    @FXML
    void signOutClick(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
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
