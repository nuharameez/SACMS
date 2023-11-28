package com.example.oodcw;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DataTruncation;


public class Login {


    //composition used here
    private SacmsDatabaseConnector databaseConnector;


    Sacms sacms = new Sacms();
    ObservableList<String> userOption = FXCollections.observableArrayList( "Student","Club Advisor");
    @FXML
    private ChoiceBox userSelect;
    @FXML
    private Label loginError;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    public Login(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }

    @FXML
    private void initialize(){

        userSelect.setItems(userOption);
    }

    //1.1.2.1 ----> login sequence
    @FXML
    protected void onLoginButtonClick(ActionEvent actionEvent){
        //1.1.2 ----> login sequence
        String usernameField = username.getText().toLowerCase();
        String passwordField = password.getText();
        String selectedRole = (String) userSelect.getValue();
        //error login option here since the controller for login is in startpage.fxml
        if (usernameField.isEmpty() || passwordField.isEmpty() || selectedRole == null || selectedRole.isEmpty()){
            loginError.setText("Please Enter Login Details");
        }
        else{
            loginError.setText("");
            Connection connection = databaseConnector.dbConnector();
            String role = (selectedRole.equalsIgnoreCase("student"))? "student" : "clubadvisor";
            UserDetails userDetails = databaseConnector.getUserDetails(role, usernameField.toLowerCase(), connection); //to store current user
            boolean accountExists = databaseConnector.authenticateUser(role, usernameField.toLowerCase(), passwordField, connection);

            //1.1.2.4 ----> login sequence
            if (accountExists) {
                //getting the id and name of the user to store seperaretly and use in the join club class
                //inside the if so it will work only if a valid username is entered.
                if(role.equalsIgnoreCase("student")) {
                    String userId = userDetails.getId();
                    String userName = userDetails.getName();
                    JoinClub.setUserDetails(userId, userName); //setting the name and id to use in the join club class.
                    sacms.openMenu(selectedRole);
                }
                else if (role.equalsIgnoreCase("clubadvisor")) {
                    String userId = userDetails.getId();
                    String userName = userDetails.getName();
                    //JoinClub.setUserDetails(userId, userName); //setting the name and id to use in the join club class.
                    sacms.openMenu(selectedRole);
                }
                Stage previousStage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                previousStage.close();

                //1.1.2.6 & 1.1.2.7 ----> login sequence
            } else {
                loginError.setText("Invalid username or password");
            }

        }
    }

    //1.1.3 & 1.1.3.1 ----> login sequence
    @FXML
    private void handleRegisterLink(ActionEvent event) {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("register.fxml")); //1.1.3.2 ----> login sequence
        } catch (IOException e) {
            System.out.println("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
        Stage previousStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        previousStage.close();
    }





}