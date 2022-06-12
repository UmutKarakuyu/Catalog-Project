module com.example.catalogproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.web;

    opens com.example.catalog to javafx.fxml;
    exports com.example.catalog;
}