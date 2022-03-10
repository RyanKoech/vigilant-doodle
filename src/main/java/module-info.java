module com.example.vigilantdoodle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires jfoenix;
    requires java.sql;
    requires fontawesomefx;

    opens com.example.vigilantdoodle to javafx.fxml;
    exports com.example.vigilantdoodle;
}