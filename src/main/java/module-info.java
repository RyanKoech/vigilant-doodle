module com.example.vigilantdoodle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.graphics;

    opens com.example.vigilantdoodle to javafx.fxml;
    exports com.example.vigilantdoodle;
}