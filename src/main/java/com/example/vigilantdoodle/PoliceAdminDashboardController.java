package com.example.vigilantdoodle;

import com.example.vigilantdoodle.utilities.Data;
import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PoliceAdminDashboardController implements Initializable {

    @FXML
    private JFXButton dashboardMenuButton;

    @FXML
    private JFXButton reportingMenuButton;

    @FXML
    private JFXButton custodiesMenuButton;

    @FXML
    private JFXButton addPoliceMenuButton;

    @FXML
    private JFXButton editPoliceMenuButton;

    @FXML
    private Label welcomeBannerLabel;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private VBox reportingTabVBox;

    @FXML
    private TextField reporterNameTextField;

    @FXML
    private TextField reporterIdTextField;

    @FXML
    private TextField offenderIdTextField;

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
    private ChoiceBox<?> custodyTypeChoiceBox;

    @FXML
    private Label bailFeeLabel;

    @FXML
    private JFXButton updateCustodyButton;

    @FXML
    private VBox addPoliceTabVBox;

    @FXML
    private TextField addPolicePoliceIdTextField;

    @FXML
    private TextField addPolicePoliceNameTextField;

    @FXML
    private TextField addPolicePhoneNumberTextField;

    @FXML
    private TextField addPoliceEmailAddressTextField;

    @FXML
    private ChoiceBox<?> addPolicePoliceRoleChoiceBox;

    @FXML
    private JFXButton addPoliceButton;

    @FXML
    private VBox editPoliceTabVBox;

    @FXML
    private TextField seachPoliceTextField;

    @FXML
    private JFXButton searchPoliceButton;

    @FXML
    private TextField editPolicePoliceIdTextField;

    @FXML
    private TextField editPolicePoliceNameTextField;

    @FXML
    private TextField editPolicePoliceNumberTextField;

    @FXML
    private TextField editPoliceEmailAddressTextField;

    @FXML
    private JFXButton updatePoliceButton;

    @FXML
    private JFXButton deletePoliceButton;

    @FXML
    private VBox dashboardTabVBox;

    @FXML
    private Label fact1Label;

    @FXML
    private Label stat1Label;

    @FXML
    private Label fact2Label;

    @FXML
    private Label stat2Label;

    @FXML
    private Label fact3Label;

    @FXML
    private Label stat3Label;

    @FXML
    private JFXTreeTableView<?> dashboardTableView;

    @FXML
    private TreeTableColumn<?, ?> obIdTableColumn;

    @FXML
    private TreeTableColumn<?, ?> policeNameTableColumn;

    @FXML
    private TreeTableColumn<?, ?> offenderNameTableColumn;

    @FXML
    private TreeTableColumn<?, ?> locationTableColumn;

    @FXML
    private TreeTableColumn<?, ?> dateTableColumn;

    @FXML
    private TreeTableColumn<?, ?> timeTableColumn;

    @FXML
    private TreeTableColumn<?, ?> crimeTableColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWelcomeBannerLabel();
    }

    @FXML
    void onAddPolice(ActionEvent event) {

    }

    @FXML
    void onDeletePolice(ActionEvent event) {

    }

    @FXML
    void onReportCrime(ActionEvent event) {

    }

    @FXML
    void onSearchCase(ActionEvent event) {

    }

    @FXML
    void onSearchPolice(ActionEvent event) {

    }

    @FXML
    void onUpdateCustody(ActionEvent event) {

    }

    @FXML
    void onUpdatePolice(ActionEvent event) {

    }

    //Sets the logged in police name on the welcome banner
    private void setWelcomeBannerLabel() {
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {

                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT Name FROM `users` WHERE `Police_Id` = ?");
                st.setString(1, Data.POLICE_ID);
                ResultSet res = st.executeQuery();

                while (res.next()) {
                    String welcomeName = res.getString("Name");
                    welcomeBannerLabel.setText("Inspector " + welcomeName);

                }
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

}
