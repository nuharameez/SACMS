package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;

public class ClubMembership {
    @FXML
    private ChoiceBox selectClub;
    @FXML
    private TextField clubAdvisor;
    @FXML
    private TextField studentCount;
    @FXML
    private TableView viewStudentsInClub;
    @FXML
    private TableColumn studentID;
    @FXML
    private TableColumn studentName;
    @FXML
    private Button viewReport;

    ObservableList<String> clubOption = FXCollections.observableArrayList();

    private SacmsDatabaseConnector databaseConnector;
    public ClubMembership(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }

    @FXML
    protected void initialize() {

        databaseConnector.clubOptions(clubOption);
        selectClub.setItems(clubOption);
    }

    @FXML
    protected void onViewReportClick(ActionEvent actionEvent){
        //search club table for selectedClub and get club advisor name.
        String selectedClub = (String) selectClub.getValue();
        Connection connection = databaseConnector.dbConnector();
        String clubAdvisorName = databaseConnector.getClubAdvisorName(selectedClub, connection);
        clubAdvisor.setText(clubAdvisorName);
        //convert club name to one word without spaces
        //set clubAdvisor to club advisor name
        //access the clubName table
        //get number of students
        int stuCount = databaseConnector.getStudentCount(selectedClub.replaceAll("\\s", "").toLowerCase(),connection);
        String noOfStudent = String.valueOf(stuCount);
        studentCount.setText(noOfStudent);
        //display student in table
        ObservableList<Student> studentData = databaseConnector.getStudents(selectedClub.replaceAll("\\s", "").toLowerCase(),connection);
        studentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        viewStudentsInClub.setItems(studentData);
    }
    @FXML
    void menuClick(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clubadvisormenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
    }

}
