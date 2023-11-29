package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ClubReportNew implements Initializable {

    private SacmsDatabaseConnector databaseConnector;

    @FXML
    private javafx.scene.control.TableView<Clubs> TableView;

    @FXML
    private TableColumn<Clubs, String> nameColumn;

    @FXML
    private TableColumn<Clubs, String> categoryColumn;

    @FXML
    private TableColumn<Clubs, String> mottoColumn;

    public ClubReportNew(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }

    @FXML
    void backToMenuClick(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clubadvisormenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("clubName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("clubCategory"));
        mottoColumn.setCellValueFactory(new PropertyValueFactory<>("clubMotto"));
        displayClubs();
    }
    public void displayClubs() {
        try {

            // Get the clubs from the database
            ObservableList<Clubs> listOfClubs = FXCollections.observableArrayList();

            Connection connection =databaseConnector.dbConnector();

            // get all clubs in the clubs table
            String allClubs = "SELECT * FROM clubsTable";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(allClubs)) {

                while (resultSet.next()) {
                    int clubID = resultSet.getInt("clubID");
                    String clubName = resultSet.getString("clubName");
                    String clubCategory = resultSet.getString("clubCategory");
                    String clubAdvisor = resultSet.getString("clubAdvisor");
                    String clubMotto = resultSet.getString("clubMotto");

                    Clubs club = new Clubs(clubID, clubName, clubCategory, clubAdvisor, clubMotto);
                    listOfClubs.add(club);
                }
            }

            // Set the items in the TableView of clubs
            TableView.setItems(listOfClubs);

            // Close the database connection
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
