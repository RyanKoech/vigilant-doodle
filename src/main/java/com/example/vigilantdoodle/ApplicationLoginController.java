package com.example.vigilantdoodle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ApplicationLoginController {
    private static final String POLICE_STATE = "police";
    private static final String ADMIN_STATE = "admin";
    private static final String INVESTIGATING_STATE = "investigating";

    private String loginState = POLICE_STATE;

    @FXML
    private JFXButton adminButton;

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordTextfield;

    @FXML
    private JFXButton investigatingTextField;

    @FXML
    private Label messageLabel;


    @FXML
    void loginUser(ActionEvent event) {

    }

    @FXML
    void setInvestigatingLoginState(ActionEvent event) {

    }

    @FXML
    void toggleAdminLoginState(ActionEvent event) {
        if(loginState.equals(ADMIN_STATE)){
            loginState = POLICE_STATE;
        }else if(loginState.equals(POLICE_STATE)){
            loginState = ADMIN_STATE;
        }

        adminButton.setText(loginState.substring(0, 1).toUpperCase() + loginState.substring(1).toLowerCase());
        messageLabel.setText(loginState);
    }
}