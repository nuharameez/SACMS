package com.example.oodcw;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.sql.SQLException;

//get clubs from database and populate to the choice box
//when join button is clicked, the club needs to be saved with the students name and other details
//when clear is clicked a new window needs to be opened.
public class JoinClub {
    @FXML
    private ChoiceBox selectClub;
    @FXML
    private Label joinMessage;
    ObservableList<String> clubOption = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {

        SacmsDatabaseConnector.clubOptions(clubOption);
        selectClub.setItems(clubOption);
    }
    @FXML
    protected void onJoinButtonClick(ActionEvent actionEvent)  {
        //when join button clicked, the relevant table needs to add the student name in it.
        joinMessage.setText("You have joined the club successfully");
    }
}
