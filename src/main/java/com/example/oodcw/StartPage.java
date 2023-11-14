package com.example.oodcw;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPage extends Application {
    ObservableList<String> userOption = FXCollections.observableArrayList( "Student","Club Advisor");
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Hyperlink registerlink;
    @FXML
    private Button login;
    @FXML
    private ChoiceBox userSelect;

    @FXML
    private void initialize(){
        userSelect.setItems(userOption);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("startpage.fxml"));
        AnchorPane root = loader.load();

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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Registration Type");
        alert.setHeaderText("Are you a student or a club advisor?");
        alert.setContentText("Choose your role: ");

        ButtonType studentButton = new ButtonType("Student");
        ButtonType clubadvisorButton = new ButtonType("Club Advisor");

        alert.getButtonTypes().setAll(studentButton, clubadvisorButton);

        //showAndWait: displays alert and waits for user to interact
        //.ifPresent(buttonType -> {--}: optional method. takes a lambda expression as a parameter.
        //performs action when user interacts with the dialog.
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == studentButton) {
                try {
                    navigateToRegistrationPage("registerstudent.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (buttonType == clubadvisorButton) {
                try {
                    navigateToRegistrationPage("registerclubadvisor.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void navigateToRegistrationPage(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        AnchorPane root = loader.load();

        Stage registrationStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        registrationStage.setScene(scene);
        registrationStage.setTitle("Registration Page");
        registrationStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
