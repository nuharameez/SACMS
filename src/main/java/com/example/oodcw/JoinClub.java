package com.example.oodcw;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.SQLException;


//get clubs from database and populate to the choice box
//when join button is clicked, the club needs to be saved with the students name and other details
//when clear is clicked a new window needs to be opened.
public class JoinClub {


    @FXML
    private ChoiceBox selectClub;
    @FXML
    private Label joinMessage;
    @FXML
    private Label errorMessage;
    ObservableList<String> clubOption = FXCollections.observableArrayList();
    private SacmsDatabaseConnector databaseConnector;
    private static String userId;
    private static String userName;
    public JoinClub(){
        this.databaseConnector = new SacmsDatabaseConnector();
    }





    public static void setUserDetails(String userId, String userName){
        JoinClub.userId = userId;
        JoinClub.userName = userName;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getUserName() {
        return userName;
    }



    @FXML
    protected void initialize() {

        databaseConnector.clubOptions(clubOption);
        selectClub.setItems(clubOption);
    }
    @FXML
    protected void onJoinButtonClick(ActionEvent actionEvent) throws SQLException {
        //when join button clicked, the relevant table needs to add the student name in it.
        setUserDetails(userId, userName);
        String clubName = (String) selectClub.getValue();
        System.out.println(clubName.toLowerCase());
        Connection connection = databaseConnector.dbConnector();
        boolean checkId = databaseConnector.authenticateJoinClub(clubName, getUserId(), connection);
        if (checkId){
            joinMessage.setText("");
            errorMessage.setText("You have already erolled in this club");
        }
        else {
            errorMessage.setText("");
            joinMessage.setText("You have joined the club successfully");
            databaseConnector.addStudentToClubTable(getUserId(), getUserName(), clubName.toLowerCase(), connection);
        }
        //System.out.println("checking..." + getUserId() + getUserName());






        //getUserInfo method comes here
    }




}