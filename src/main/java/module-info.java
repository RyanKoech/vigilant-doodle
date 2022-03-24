module com.example.vigilantdoodle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires com.jfoenix;
    requires java.sql;

    opens com.example.vigilantdoodle to javafx.fxml;
    opens com.example.vigilantdoodle.datamodels to javafx.fxml;

    exports com.example.vigilantdoodle.datamodels;
    exports com.example.vigilantdoodle;
}