package com.example.vigilantdoodle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class PoliceInvestigatingDashboardController implements Initializable {

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
    private ChoiceBox<?> crimeIdChoiceBox;

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

    //METHODS
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void addEvidence(ActionEvent event) {

    }

    @FXML
    void deleteEvidence(ActionEvent event) {

    }

    @FXML
    void onLogout(ActionEvent event) {

    }

    @FXML
    void onShowDahboardTab(ActionEvent event) {

    }

    @FXML
    void onShowEvidenceTab(ActionEvent event) {

    }

    @FXML
    void updateEvidence(ActionEvent event) {

    }

}
