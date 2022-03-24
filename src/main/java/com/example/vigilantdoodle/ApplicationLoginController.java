package com.example.vigilantdoodle;

import com.example.vigilantdoodle.utilities.Data;
import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
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
        loginState = INVESTIGATING_STATE;
    }

    @FXML
    void onLoginButtonClick(ActionEvent event) {
        //Ensure no fields are empty
        if (passwordTextfield.getText().isEmpty() || obNumberTextfield.getText().isEmpty()){
            messageLabel.setText("Please Ensure Password and Id field are filled");
            return;
        }
        loginUser(obNumberTextfield.getText(), passwordTextfield.getText(), event);
    }

    @FXML
    void onToggleAdminClick(ActionEvent event) {
        if(adminButton.getText().equalsIgnoreCase(ADMIN_STATE)){
            loginState = ADMIN_STATE;
            adminButton.setText(POLICE_STATE.substring(0, 1).toUpperCase() + POLICE_STATE.substring(1).toLowerCase());
        }else if(adminButton.getText().equalsIgnoreCase(POLICE_STATE)){
            loginState = POLICE_STATE;
            adminButton.setText(ADMIN_STATE.substring(0, 1).toUpperCase() + ADMIN_STATE.substring(1).toLowerCase());
        }
    }

    void loginUser(String policeId, String password, ActionEvent event) {
        Connection connection = MysqlConnector.connectDB();
        if(connection != null){
            try{
                PreparedStatement statement = (PreparedStatement)
                        connection.prepareStatement ("SELECT * FROM "+ getUserTables() + " WHERE `Police_Id` = ? and password = ?");
                statement.setString(1, policeId);
                statement.setString(2, password);
                ResultSet res = statement.executeQuery();

                if(res.next()){
                    messageLabel.setText("Logged in successfully");
                    Data.POLICE_ID = policeId;
                    navigate(event);
                }else{
                    messageLabel.setText("Invalid Credentials for " + loginState);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Logger.getLogger(ApplicationLoginController.class.getName()).log(Level.SEVERE, null, throwables.getMessage());
            }
        }else {
            messageLabel.setText("Error with Connecting with Database");
        }
    }

    String getUserTables(){
        return switch (loginState) {
            case ADMIN_STATE -> "`police admin`";
            case POLICE_STATE -> "`police`";
            case INVESTIGATING_STATE -> "`police investigating`";
            default -> "";
        };
    }

    void navigate(ActionEvent event){
        try{
            Parent menuParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(getDashboardFXMLs())));
            Scene menuScene = new Scene(menuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    String getDashboardFXMLs(){
        return switch (loginState) {
            case ADMIN_STATE -> "police-admin-dashboard.fxml";
            case POLICE_STATE -> "police-dashboard.fxml";
            case INVESTIGATING_STATE -> "police-investigating-dashboard.fxml";
            default -> "";
        };
    }

}