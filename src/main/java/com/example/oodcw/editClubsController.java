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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class editClubsController implements Initializable {

    private SacmsDatabaseConnector databaseConnector;

    @FXML
    private TextField clubId;

    @FXML
    private TextField newClubName;

    @FXML
    private TextField clubCategory;

    @FXML
    private TextField clubAdvisor;

    @FXML
    private TextField clubMotto;

    @FXML
    private Label incompleteFields;

    @FXML
    private TableView<Clubs> TableView;

    @FXML
    private TableColumn<Clubs, Integer> idColumn;

    @FXML
    private TableColumn<Clubs, String> nameColumn;

    @FXML
    private TableColumn<Clubs, String> categoryColumn;

    @FXML
    private TableColumn<Clubs, String> advisorColumn;

    @FXML
    private TableColumn<Clubs, String> mottoColumn;

    @FXML
    void backToMenuClick(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clubadvisormenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void updateButton(ActionEvent event) throws Exception {
        String updatedName = newClubName.getText();
        String updatedCategory = clubCategory.getText();
        String updatedClubAdvisor = clubAdvisor.getText();
        String updatedMotto = clubMotto.getText();

        // Check if clubId is empty
        if (clubId.getText().isEmpty()) {
            System.out.println("Club ID cannot be empty!");
            incompleteFields.setText("Club ID cannot be empty!");
            return;
        }

        // Parse clubId only if it's not empty
        int clubID = Integer.parseInt(clubId.getText());

        Connection connection = databaseConnector.dbConnector();

        // Checking if all the fields are filled
        if (updatedName.isEmpty() || updatedCategory.isEmpty() || updatedClubAdvisor.isEmpty() || updatedMotto.isEmpty() || clubID==0) {
            incompleteFields.setText("Please complete all the fields.");
            return; // Exit the method if validation fails

            // Checking if club advisor contains only strings
        } else if (!updatedClubAdvisor.matches("[a-zA-Z ]+")) {
            incompleteFields.setText("Please enter a valid name for club advisor");
            return;

        }

        databaseConnector.updateClub(updatedName, updatedCategory, updatedClubAdvisor, updatedMotto, clubID, connection);
        backToMenuClick(event);

        displayClubs();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("clubID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("clubName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("clubCategory"));
        advisorColumn.setCellValueFactory(new PropertyValueFactory<>("clubAdvisor"));
        mottoColumn.setCellValueFactory(new PropertyValueFactory<>("clubMotto"));
        displayClubs();
    }
    public void displayClubs() {
        try {

            // Get the clubs from the database
            ObservableList<Clubs> listOfClubs = FXCollections.observableArrayList();

            Connection connection = databaseConnector.dbConnector();

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

