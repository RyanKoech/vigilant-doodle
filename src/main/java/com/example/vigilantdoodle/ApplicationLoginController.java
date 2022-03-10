package com.example.vigilantdoodle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ApplicationLoginController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}