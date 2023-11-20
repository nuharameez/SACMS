package com.example.oodcw;

//import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;

public class ClubCreation {

        @FXML
        private TextField clubCategory;

        @FXML
        private TextField clubMotto;

        @FXML
        private TextField clubName;

        @FXML
        private TextField nameOfClubAdvisor;



        @FXML
        public void saveClubClick(ActionEvent event) throws Exception {
                String Name = clubName.getText();
                String Category = clubCategory.getText();
                String Advisor = nameOfClubAdvisor.getText();
                String Motto = clubMotto.getText();

                // insert into the database
                checkDetailsEvent(Name, Category, Advisor, Motto);
        }

        public void checkDetailsEvent(String Name, String Category, String Advisor, String Motto) {

        }

}
