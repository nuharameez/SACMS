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

    @FXML
    private void initialize(){
        role.setItems(roleOption);
    }



    //when the registration button is clicked
    //1. checks if all fields filled
    //2. checks if the 2 passwords match
    //3. opens menu according to the role of the user
    @FXML
    protected void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
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
        else{
            registerError.setText("");
            openMenu(roleField); //opens relevant menu
        }


    }

    //method to open the relevant menu according to role
    private void openMenu(String selectedRole) throws IOException {
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

}

//connect to database and work on saving.
