module com.example.oodcw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.oodcw to javafx.fxml;
    exports com.example.oodcw;
}