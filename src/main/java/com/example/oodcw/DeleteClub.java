package com.example.oodcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

public class DeleteClub {

    private SacmsDatabaseConnector databaseConnector;

    @FXML
    private TextField clubName;

    @FXML
    private Button deleteClub;

    @FXML
    private Label invalidName;

    public DeleteClub(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }

    @FXML
    void backToMenuClick(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clubadvisormenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onDeleteClubClick(ActionEvent event) throws Exception{
        String clubToDelete = clubName.getText();
        clubToDelete = clubToDelete.toLowerCase();

        Connection connection = databaseConnector.dbConnector();

        if (clubToDelete.isEmpty()){
            invalidName.setText("Please enter the name of the club to delete!");
            return;
        } else if (databaseConnector.clubExists(clubToDelete, connection)){
            // deleteClub method is called
            databaseConnector.deleteClub(clubToDelete, connection);
            System.out.println("Club deleted successfully!");
            backToMenuClick(event);
        }
        else{
            invalidName.setText("Could not delete club!");
        }


    }

}
