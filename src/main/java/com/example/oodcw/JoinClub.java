package com.example.oodcw;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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




    //1.1.1 ----> join club sequence
    public static void setUserDetails(String userId, String userName){
        JoinClub.userId = userId;
        JoinClub.userName = userName;
    }
    //1.1.1.1.2 ----> join club sequence
    public static String getUserId() {
        return userId;
    }
    //1.1.1.1.3 ----> join club sequence
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
        //1.1.1.1.4.2 & 1.1.1.1.4.3 ----> join club sequence
        if (checkId){
            joinMessage.setText("");
            errorMessage.setText("You have already erolled in this club");
        }
        //1.1.1.1.1 ----> join club sequence
        else {
            errorMessage.setText("");
            databaseConnector.addStudentToClubTable(getUserId(), getUserName(), clubName.replaceAll("\\s", "").toLowerCase(), connection);
            //spaces removed from club name to make creating club table easier.
            joinMessage.setText("You have joined the club successfully"); //1.1.1.1.4.1 ----> join club sequence

        }
        //System.out.println("checking..." + getUserId() + getUserName());


        //getUserInfo method comes here
    }

    @FXML
    void signOutClick(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("studentmenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main menu");
        stage.setScene(scene);
        stage.show();
    }



}