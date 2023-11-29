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

public class EditClub implements Initializable {

    private SacmsDatabaseConnector databaseConnector;

    @FXML
    private TextField clubId;

    @FXML
    private TextField newClubName;

    @FXML
    private TextField newClubCategory;

    @FXML
    private TextField newClubAdvisor;

    @FXML
    private TextField newClubMotto;

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

    public EditClub(){
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

    @FXML
    void onClickToEdit(ActionEvent event) throws SQLException {
        // Check if clubId is empty
        if (clubId.getText().isEmpty()) {
            incompleteFields.setText("Please enter club ID!");
            return;
        }

        // Parse clubId only if it's not empty
        int clubID = Integer.parseInt(clubId.getText());
        // Pre-fill the fields with the details of the selected club
        showClubDetails(clubID);

        Connection connection = databaseConnector.dbConnector();

        // Checking if the clubID exists in the database
        if (!clubIDExists(clubID, connection)) {
            incompleteFields.setText("ClubID " + clubID + " does not exist!");
        }
    }

    @FXML
    public void onEditClubClick(ActionEvent event) throws Exception {
        String updatedName = newClubName.getText();
        String updatedCategory = newClubCategory.getText();
        String updatedClubAdvisor = newClubAdvisor.getText();
        String updatedMotto = newClubMotto.getText();

        updatedCategory = updatedCategory.toLowerCase();
        updatedName = updatedName.toLowerCase();

        // Check if clubId is empty
        if (clubId.getText().isEmpty()) {
            incompleteFields.setText("Please enter club ID!");
            return;
        }

        // Parse clubId only if it's not empty
        int clubID = Integer.parseInt(clubId.getText());

        Connection connection = databaseConnector.dbConnector();

        // Checking if all the fields are filled
        if (updatedName.isEmpty() || updatedCategory.isEmpty() || updatedClubAdvisor.isEmpty() || updatedMotto.isEmpty()) {
            incompleteFields.setText("Please complete all the fields.");
            return; // Exit the method if validation fails
        }

        // Checking if the clubID exists in the database
        if (!clubIDExists(clubID, connection)) {
            incompleteFields.setText("ClubID " + clubID + " does not exist!");
            return;
        }

        // Checking if club advisor contains only strings
        if (!updatedClubAdvisor.matches("[a-zA-Z ]+")) {
            incompleteFields.setText("Please enter a valid name for club advisor");
            return;
        }

        databaseConnector.editClub(updatedName, updatedCategory, updatedClubAdvisor, updatedMotto, clubID, connection);
        backToMenuClick(event);
        displayClubs();
    }


    // method that set text on the fields
    private void showClubDetails(int clubID) {
        Connection connection = databaseConnector.dbConnector();
        try {
            String getClubDetailsQuery = "SELECT * FROM clubsTable WHERE clubID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(getClubDetailsQuery)) {
                preparedStatement.setInt(1, clubID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String clubName = resultSet.getString("clubName");
                        String clubCategory = resultSet.getString("clubCategory");
                        String clubAdvisor = resultSet.getString("clubAdvisor");
                        String clubMotto = resultSet.getString("clubMotto");

                        // Set the extracted details of each club in the text fields
                        newClubName.setText(clubName);
                        newClubCategory.setText(clubCategory);
                        newClubAdvisor.setText(clubAdvisor);
                        newClubMotto.setText(clubMotto);

                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
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

    //check if clubID already exists
    private boolean clubIDExists(int clubID, Connection connection) throws SQLException {
        String checkClubExistenceQuery = "SELECT * FROM clubsTable WHERE clubID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkClubExistenceQuery)) {
            preparedStatement.setInt(1, clubID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If there is a result, the club exists
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}

