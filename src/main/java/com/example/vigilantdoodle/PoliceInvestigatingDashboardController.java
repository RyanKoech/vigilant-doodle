package com.example.vigilantdoodle;

import com.example.vigilantdoodle.utilities.Data;
import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PoliceInvestigatingDashboardController implements Initializable {

    @FXML
    private VBox dashboardTabVBox;

    @FXML
    private VBox evidenceTabVBox;

    @FXML
    private JFXButton dashboardMenuButton;

    @FXML
    private JFXButton evidenceMenuButton;

    @FXML
    private Label welcomeBannerLabel;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private ChoiceBox<?> evidenceIdChoiceBox;

    @FXML
    private TextArea evidenceDescription;

    @FXML
    private JFXButton addEvidenceButton;

    @FXML
    private JFXButton updateEvidenceButton;

    @FXML
    private JFXButton deleteEvidenceButton;

    @FXML
    private ChoiceBox<String> crimeIdChoiceBox;

    @FXML
    private Label crimeTypeLabel;

    @FXML
    private Label suspectNameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private TextArea caseDescriptionTextArea;

    //NON FXML PROPERTIES

    //METHODS
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCrimeIdChoiceBoxItems();
        crimeIdChoiceBox.getSelectionModel().selectedItemProperty().addListener((v , oldValue, newValue) -> getCaseInformation(newValue));
    }

    @FXML
    private void onShowDahboardTab(ActionEvent event) {
        dashboardTabVBox.toFront();
    }

    @FXML
    private void onShowEvidenceTab(ActionEvent event) {
        evidenceTabVBox.toFront();
    }


    //Logout Button Function
    @FXML
    private void onLogout(ActionEvent event) {
        try {
            Parent menuParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("application-login.fxml")));
            Scene menuScene = new Scene(menuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addEvidence(ActionEvent event) {

    }

    @FXML
    private void deleteEvidence(ActionEvent event) {

    }

    @FXML
    private void updateEvidence(ActionEvent event) {

    }

    private void setCrimeIdChoiceBoxItems(){
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("SELECT `cases`.`OB_id` FROM `cases` WHERE cases.Investigator_Id = ?");
                statement.setString(1, Data.POLICE_ID);
                ResultSet resultSet = statement.executeQuery();

                crimeIdChoiceBox.getItems().clear();
                while (resultSet.next()) {
                    String obId = resultSet.getString("OB_id");

                    //Add crime type to ChoiceBox
                    crimeIdChoiceBox.getItems().addAll(obId);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getCaseInformation(String obId){
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("SELECT `crime types`.`Type`, `citizens`.`Name`, `Date`, `Time`, `Location`,`Description` FROM `cases` INNER JOIN `crime types` ON `cases`.`Crime_Type` = `crime types`.`Type_Id` INNER JOIN `citizens` ON `cases`.`Offender_Id` = `citizens`.`National ID` WHERE `cases`.`OB_id` = ?");
                statement.setString(1, obId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    crimeTypeLabel.setText(resultSet.getString("Type"));
                    suspectNameLabel.setText(resultSet.getString("Name"));
                    dateLabel.setText(resultSet.getString("Date"));
                    timeLabel.setText(resultSet.getString("Time"));
                    locationLabel.setText(resultSet.getString("Location"));
                    caseDescriptionTextArea.setText("Description");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
