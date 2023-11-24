package com.example.oodcw;

import javafx.application.Application;
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


public class Login extends Application {
    ObservableList<String> userOption = FXCollections.observableArrayList( "Student","Club Advisor");

    @FXML
    private ChoiceBox userSelect;
    @FXML
    private Hyperlink registerlink;
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

    @Override
    public void start(Stage primaryStage){
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("startpage.fxml"));
        } catch (IOException e) {
            System.out.println("Cannot start application");;
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();

    }

    @FXML
    private void handleRegisterLink() {
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("register.fxml"));
        } catch (IOException e) {
            loginError.setText("Failed to open register page");
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent actionEvent) throws Exception{
        String usernameField = username.getText();
        String passwordField = password.getText();
        String selectedRole = (String) userSelect.getValue();
        //error login option here since the controller for login is in startpage.fxml
        if (usernameField.isEmpty() || passwordField.isEmpty() || selectedRole == null || selectedRole.isEmpty()){
            loginError.setText("Please Enter Login Details");
        }
        else{
            loginError.setText("");
            Connection connection = SacmsDatabaseConnector.dbConnector();
            String role = (selectedRole.equalsIgnoreCase("student"))? "student" : "clubadvisor";
            boolean accountExists = SacmsDatabaseConnector.authenticateUser(role, usernameField, passwordField, connection);
            if (accountExists) {
                openMenu(selectedRole);
            } else {
                loginError.setText("Invalid username or password");
            }

            //code to check if student or club advisor and open the relevant menu
        }
    }
    protected void openMenu(String selectedRole) {
        Stage newStage = new Stage();
        if(selectedRole.equals("Student")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("studentmenu.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newStage.setScene(new Scene(root, 600, 400));
        }
        else{
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("clubadvisormenu.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newStage.setScene(new Scene(root, 600, 400));
        }
        newStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
