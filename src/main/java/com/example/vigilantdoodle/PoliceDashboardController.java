package com.example.vigilantdoodle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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

    //Logout Button Function
    @FXML
    void onLogout(ActionEvent event) {
        try{
            Parent menuParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("application-login.fxml")));
            Scene menuScene = new Scene(menuParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Side Menu Navigation Button Actions
    //No 1
    @FXML
    void onShowCustodiesTab(ActionEvent event) {
        custodiesTabVBox.toFront();
    }

    //No 2
    @FXML
    void onShowDashboardTab(ActionEvent event) {
        dashboardTabVBox.toFront();
    }

    //No 3
    @FXML
    void onShowReportingTab(ActionEvent event) {
        reportingTabVBox.toFront();
    }

    @FXML
    void onUpdateCustody(ActionEvent event) {

    }

    @FXML
    void onSearchCase(ActionEvent event) {

    }

}
