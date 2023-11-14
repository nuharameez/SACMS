module com.example.oodcw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oodcw to javafx.fxml;
    exports com.example.oodcw;
}