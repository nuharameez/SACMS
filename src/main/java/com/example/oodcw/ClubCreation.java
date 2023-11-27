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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class ClubCreation {

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

                // Checking if all the field are filled
                if (clubName.isEmpty() || clubCategory.isEmpty() || clubAdvisor.isEmpty() || clubMotto.isEmpty()) {
                        incompleteFields.setText("Please complete all the fields.");
                        return; // Exit the method if validation fails
                }

                // Inserting data to the clubs table
                checkDetailsEvent(clubName, clubCategory, clubAdvisor, clubMotto);
        }

        public void checkDetailsEvent(String Name, String Category, String Advisor, String Motto) {
                try {
                        // Load the JDBC driver
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        // connecting to the database
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/scams", "root", "strawberry@2002");



                        // SQL statement
                        String sql = "INSERT INTO clubsTable (clubName, clubCategory, clubAdvisor, clubMotto) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setString(1, Name);
                                preparedStatement.setString(2, Category);
                                preparedStatement.setString(3, Advisor);
                                preparedStatement.setString(4, Motto);

                                // Execute the update statement
                                preparedStatement.executeUpdate();


                        }

                        // Close the connection
                        connection.close();

                        System.out.println("Club saved to the CLUBS database!");
                } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                }
        }

}