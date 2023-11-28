package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class Register {

    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private ChoiceBox<String> role;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repassword;
    @FXML
    private Label registerError;

    //composition used here
    private SacmsDatabaseConnector databaseConnector;


    ObservableList<String> roleOption = FXCollections.observableArrayList( "Student","Club Advisor");
    Sacms sacms = new Sacms();

    public Register(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }

    //populating the choice box
    @FXML
    private void initialize(){
        role.setItems(roleOption);
    }

    //when the registration button is clicked
    //1. checks if all fields filled
    //2. checks if the 2 passwords match
    //3. opens menu according to the role of the user

    //1.1 ----> register sequence
    @FXML
    protected void onRegisterButtonClick(ActionEvent actionEvent) {
        //1. Enter register details ----> register sequence
        String nameField = name.getText();
        String idField = id.getText();
        String roleField = (String) role.getValue();
        String usernameField = username.getText().toLowerCase();
        String passwordField = password.getText();
        String repasswordField = repassword.getText();

        //checks for empty fields
        if (nameField.isEmpty() || roleField == null || idField.isEmpty() || usernameField.isEmpty() || passwordField.isEmpty() || repasswordField.isEmpty()) {
            registerError.setText("Please fill in all fields");

        } else if (!passwordField.matches(repasswordField)) { //checks for mathcing password
            registerError.setText("Passwords do not match");
        }
        else if(usernameField.contains(" ")){
            registerError.setText("Username cannot include spaces");
        }
        else{
            registerError.setText("");

            Connection connection = databaseConnector.dbConnector();
            //since it is read as Club Advisor from the drop down but the table name is clubadvisor
            String role = (roleField.equalsIgnoreCase("student"))? "student" : "clubadvisor";
            String id = "Id";
            String username = "Username";
            boolean accountExists = databaseConnector.authenticateRegistration(role, idField, id, connection);
            boolean userExists = databaseConnector.authenticateRegistration(role, usernameField.toLowerCase(), username, connection);

            //1.1.5.6 & 1.1.5.7 ----> register sequence
            if (accountExists) {
                registerError.setText("This user id has an account");
            }
            //1.1.5.4 & 1.1.5.5 ----> register sequence
            else if(userExists){
                registerError.setText("This username is not available");
            }
            else {
                //1.1.2 & 1.1.4 & 1.1.5.1 ----> register sequence
                databaseConnector.addNewUser(role, idField, nameField, usernameField.toLowerCase(), passwordField, connection);
                JoinClub.setUserDetails(idField,usernameField.toLowerCase());
                sacms.openMenu(roleField); //1.1.5.2 ----> register sequence
                Stage previousStage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                previousStage.close();

                }
            }

        }
    @FXML
    void backToLoginClick(ActionEvent actionEvent) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();

    }


    }




