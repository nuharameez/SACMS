package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ScheduleController {
    @FXML
    private TableView<Schedule> tableView;
    @FXML
    private TableColumn<Schedule, Integer> IDColumn;
    @FXML
    private TableColumn<Schedule, String> nameColumn;
    @FXML
    private TableColumn<Schedule, String> clubColumn;
    @FXML
    private TableColumn<Schedule, String> typeColumn;
    @FXML
    private TableColumn<Schedule, LocalDate> dateColumn;
    private SacmsDatabaseConnector databaseConnector;
    Connection connection = databaseConnector.dbConnector();
    public ScheduleController() {this.databaseConnector = new SacmsDatabaseConnector();}

    @FXML
    private void initialize() {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            Connection connection = databaseConnector.dbConnector();
            List<Schedule> scheduleList = SacmsDatabaseConnector.getDataFromDatabase(connection);
            ObservableList<Schedule> observableList = FXCollections.observableArrayList(scheduleList);
            tableView.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }



    @FXML
    private void handleAddEvent(ActionEvent event) throws IOException {
        openFXML("Event.fxml", "Add Event");
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
    private void showAlert(String message) {
        // Display an alert with the given message
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleMenuButton(ActionEvent event) throws IOException{
        openFXML("ClubAdvisorMenu", "Add Activity");
    }
    @FXML
    private void handleDeleteButton(ActionEvent event) {
        // Prompt user to enter Schedule ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Schedule");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Schedule ID to delete:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(scheduleId -> {
            try {
                int id = Integer.parseInt(scheduleId);
                if (SacmsDatabaseConnector.deleteSchedule(id,connection)) {
                    List<Schedule> scheduleList = SacmsDatabaseConnector.getDataFromDatabase(connection);
                    ObservableList<Schedule> observableList = FXCollections.observableArrayList(scheduleList);
                    tableView.setItems(observableList);
                } else {
                    // Handle case where Schedule ID does not exist
                    showAlert("Schedule with ID " + id + " does not exist.");
                }
            } catch (NumberFormatException | SQLException e) {
                showAlert("Please enter valid ID");
                e.printStackTrace();
            }
        });
    }
}