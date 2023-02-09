module com.example.masa {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.masa to javafx.fxml;
    exports com.example.masa;
}