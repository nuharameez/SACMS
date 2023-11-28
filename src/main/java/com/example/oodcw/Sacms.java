package com.example.oodcw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sacms extends Application {

    //1.1 ----> login sequence diagram
    @Override
    public void start(Stage primaryStage){
        Stage newStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("startpage.fxml")); //1.1.1 ----> login sequence
        } catch (IOException e) {
            System.out.println("Cannot start application");
            e.printStackTrace();
        }
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();

    }


    //1.1.2.5 & 1.1.2.6 ----> login sequence
    //1.1.5.2 & 1.1.5.3 ----> register sequence
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

    //1. ----> login sequence
    public static void main(String[] args) {
        launch(args);
    }
}