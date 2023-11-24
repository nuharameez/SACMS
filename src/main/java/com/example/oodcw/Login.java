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
import java.sql.SQLException;

import static com.example.oodcw.SacmsDatabaseConnector.dbConnector;

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
    public void start(Stage primaryStage) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("startpage.fxml"));
        AnchorPane root = loader.load();dfghjhjj

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SCAMS");
        primaryStage.show();*/

        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("startpage.fxml"));
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();

    }

    @FXML
    private void handleRegisterLink() throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
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
    protected void openMenu(String selectedRole) throws IOException {
        Stage newStage = new Stage();
        if(selectedRole.equals("Student")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("studentmenu.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root, 600, 400));
        }
        else{
            Parent root = FXMLLoader.load(getClass().getResource("clubadvisormenu.fxml"));
            newStage.setScene(new Scene(root, 600, 400));
        }
        newStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
