module com.example.catalog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.catalog to javafx.fxml;
    exports com.example.catalog;
}