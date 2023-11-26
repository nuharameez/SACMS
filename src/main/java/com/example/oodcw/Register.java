package com.example.oodcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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


    ObservableList<String> roleOption = FXCollections.observableArrayList( "Student","Club Advisor");
    Sacms sacms = new Sacms();

    @FXML
    private void initialize(){
        role.setItems(roleOption);
    }

    //when the registration button is clicked
    //1. checks if all fields filled
    //2. checks if the 2 passwords match
    //3. opens menu according to the role of the user
    @FXML
    protected void onRegisterButtonClick(ActionEvent actionEvent) {
        String nameField = name.getText();
        String idField = id.getText();
        String roleField = (String) role.getValue();
        String usernameField = username.getText();
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

            Connection connection = SacmsDatabaseConnector.dbConnector();
            //since it is read as Club Advisor from the drop down but the table name is clubadvisor
            String role = (roleField.equalsIgnoreCase("student"))? "student" : "clubadvisor";
            boolean accountExists = SacmsDatabaseConnector.authenticateRegistration(role, idField, connection);
            boolean userExists = SacmsDatabaseConnector.authenticateUsername(role, usernameField, connection);
            if (accountExists) {
                registerError.setText("This user has an account, Please login");
            }
            else if(userExists){
                registerError.setText("This username exists");
            }
            else {
                //checks if the ID is in database
                boolean checkId = SacmsDatabaseConnector.authenticateRegistration(role, idField, connection);
                if(checkId){
                    registerError.setText("");
                    registerError.setText("This user ID already exists");

                }
                else {
                    SacmsDatabaseConnector.addNewUser(role, idField, usernameField, passwordField, nameField, connection);
                    sacms.openMenu(roleField);
                }
            }

        }


    }




}