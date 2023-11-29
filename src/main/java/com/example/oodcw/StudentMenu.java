package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class StudentMenu {

    @FXML
    protected void signOutClick(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
    }

    //method to open the join club page

    //1 & 1.1 ----> join club sequence
    @FXML
    protected void onJoinClick(ActionEvent actionEvent) throws IOException{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("joinclub.fxml"));
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
        Stage previousStage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        previousStage.close();

    }
}
