package com.example.vigilantdoodle;

import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TextField obNumberTextfield;

    @FXML
    private PasswordField passwordTextfield;

    @FXML
    private JFXButton investigatingTextField;

    @FXML
    private Label messageLabel;


    @FXML
    void onInvestigatingTextClick(ActionEvent event) {

    }

    @FXML
    void onLoginButtonClick(ActionEvent event) {
        //Ensure no fields are empty
        if (passwordTextfield.getText().isEmpty() || obNumberTextfield.getText().isEmpty()){
            messageLabel.setText("Please Ensure Password and Id field are filled");
            return;
        }
        messageLabel.setText("This ran");
        loginUser(obNumberTextfield.getText(), passwordTextfield.getText());
    }

    @FXML
    void onToggleAdminClick(ActionEvent event) {
        if(loginState.equals(ADMIN_STATE)){
            loginState = POLICE_STATE;
        }else if(loginState.equals(POLICE_STATE)){
            loginState = ADMIN_STATE;
        }

        adminButton.setText(loginState.substring(0, 1).toUpperCase() + loginState.substring(1).toLowerCase());
    }

    void loginUser(String obNumber, String password) {
        Connection connection = MysqlConnector.connectDB();
        if(connection != null){
            try{
                PreparedStatement statement = (PreparedStatement)
                        connection.prepareStatement ("SELECT * FROM `loginform` WHERE `username` = '987654321' AND `password` = '123456789' ");
//                statement.setString(1, obNumber);
//                statement.setString(2, password);
                ResultSet res = statement.executeQuery();

                if(res.next()){
                    messageLabel.setText("Logged in successfully");
                }else{
                    messageLabel.setText("Invalid Cridentials");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Logger.getLogger(ApplicationLoginController.class.getName()).log(Level.SEVERE, null, throwables.getMessage());
            }
        }else {
            messageLabel.setText("Error with Connecting with Database");
        }
    }

}