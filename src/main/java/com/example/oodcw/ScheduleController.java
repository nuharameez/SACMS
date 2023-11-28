package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ScheduleController {
    @FXML
    private TableView<Schedule> tableView;
    @FXML
    private TableColumn<Schedule, Integer> IDColumn;
    @FXML
    private TableColumn<Schedule, String> nameColumn;
    @FXML
    private TableColumn<Schedule, String> venueColumn;
    @FXML
    private TableColumn<Schedule, String> typeColumn;
    @FXML
    private TableColumn<Schedule, LocalDate> dateColumn;


    @FXML
    private void initialize() {
        // Initialize columns
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            List<Schedule> scheduleList = DatabaseOperations.getDataFromDatabase();
            ObservableList<Schedule> observableList = FXCollections.observableArrayList(scheduleList);
            tableView.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }
    @FXML
    private void handleAddEvent(ActionEvent event) throws IOException {
        openFXML("Event.fxml" ,"Add Event");
    }

    @FXML
    private void handleAddMeeting(ActionEvent event) throws IOException {
        openFXML("Meeting.fxml", "Add Meeting");
    }

    @FXML
    private void handleAddActivity(ActionEvent event) throws IOException {
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
            System.out.println("this is weird");
        }
    }


}