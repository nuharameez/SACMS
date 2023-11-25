package com.example.oodcw;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sacms extends Application {
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
