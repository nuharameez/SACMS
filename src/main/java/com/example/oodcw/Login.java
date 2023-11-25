package com.example.oodcw;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;


public class Login {


    //composition used here
    private SacmsDatabaseConnector databaseConnector;

    public Login(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }
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

    @FXML
    private void initialize(){

        userSelect.setItems(userOption);
    }


    @FXML
    protected void onLoginButtonClick(ActionEvent actionEvent){
        String usernameField = username.getText();
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
            boolean accountExists = SacmsDatabaseConnector.authenticateUser(role, usernameField, passwordField, connection);
            if (accountExists) {
                sacms.openMenu(selectedRole);
            } else {
                loginError.setText("Invalid username or password");
            }
        }
    }
    @FXML
    private void handleRegisterLink() {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("register.fxml"));
        } catch (IOException e) {
            System.out.println("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }





}