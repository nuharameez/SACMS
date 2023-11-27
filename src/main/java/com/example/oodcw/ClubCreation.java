package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

public class ClubCreation {

        private DatabaseConnectorNew databaseConnector;
        @FXML
        private TextField Category;

        @FXML
        private TextField Motto;

        @FXML
        private TextField Name;

        @FXML
        private TextField nameOfClubAdvisor;

        @FXML
        private Button saveClub;

        @FXML
        private Label incompleteFields;

        public ClubCreation(){
                this.databaseConnector = new ScamsDatabaseConnectorNew();
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
        public void saveClubClick(ActionEvent event) throws Exception {
                String clubName = Name.getText();
                String clubCategory = Category.getText();
                String clubAdvisor = nameOfClubAdvisor.getText();
                String clubMotto = Motto.getText();

                Connection connection = databaseConnector.dbConnectorNew();

                // Checking if all the field are filled
                if (clubName.isEmpty() || clubCategory.isEmpty() || clubAdvisor.isEmpty() || clubMotto.isEmpty()) {
                        incompleteFields.setText("Please complete all the fields.");
                        return; // Exit the method if validation fails

                } else if (!clubAdvisor.matches("[a-zA-Z ]+")) {
                        incompleteFields.setText("Please enter a valid name for Club Advisor");
                } else if (databaseConnector.clubExists(clubName, connection)) {
                        incompleteFields.setText("Club already exists!");
                        System.out.println("Club could not be created!");
                } else{
                        databaseConnector.createClub(clubName, clubCategory,clubAdvisor, clubMotto, connection);
                        System.out.println("Club created successfully!");
                        backToMenuClick(event);
                }

        }

}