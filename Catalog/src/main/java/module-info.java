module com.example.catalog {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.catalog to javafx.fxml;
    exports com.example.catalog;
}