package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class StudentMenu {

    private SacmsDatabaseConnector databaseConnector;
    //method to open the join club page
    //to open the view clubs page



    @FXML
    protected void onJoinClick(ActionEvent actionEvent) throws IOException{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("joinclub.fxml"));
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();

    }

    @FXML
    protected void onViewClick(ActionEvent actionEvent) throws IOException{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("viewclub.fxml"));
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }


}
