package com.example.vigilantdoodle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.VBox;

public class PoliceDashboardController {

    @FXML
    private JFXButton dashboardMenuButton;

    @FXML
    private JFXButton reportingMenuButton;

    @FXML
    private JFXButton custodiesMenuButton;

    @FXML
    private Label welcomeBannerLabel;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private VBox dashboardTabVBox;

    @FXML
    private JFXTreeTableView<?> dashboardTableView;

    @FXML
    private TreeTableColumn<?, ?> obIdTableColumn;

    @FXML
    private TreeTableColumn<?, ?> reporterNameTableColumn;

    @FXML
    private TreeTableColumn<?, ?> offenderNameTableColumn;

    @FXML
    private TreeTableColumn<?, ?> locationTableColumn;

    @FXML
    private TreeTableColumn<?, ?> dateTableColumn;

    @FXML
    private TreeTableColumn<?, ?> timeTableColumn;

    @FXML
    private TreeTableColumn<?, ?> crimeTypeTableColumn;

    @FXML
    private VBox custodiesTabVBox;

    @FXML
    private TextField obNumberTextField;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Label suspectNameLabel;

    @FXML
    private Label currentCustodyLabel;

    @FXML
    private Label custodyTypeChoiceBox;

    @FXML
    private Label bailFeeLabel;

    @FXML
    private JFXButton updateCustodyButton;

    @FXML
    private VBox reportingTabVBox;

    @FXML
    private TextField reporterNameTextField;

    @FXML
    private TextField reporterIdTextField;

    @FXML
    private TextField offenderNameTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField timeTextField;

    @FXML
    private ChoiceBox<?> crimeTypeChoiceBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private JFXButton reportButton;

    @FXML
    void onLogout(ActionEvent event) {

    }

    @FXML
    void onSearchCase(ActionEvent event) {

    }

    @FXML
    void onShowCustodiesTab(ActionEvent event) {

    }

    @FXML
    void onShowDashboardTab(ActionEvent event) {

    }

    @FXML
    void onShowReportingTab(ActionEvent event) {

    }

    @FXML
    void onUpdateCustody(ActionEvent event) {

    }

}
