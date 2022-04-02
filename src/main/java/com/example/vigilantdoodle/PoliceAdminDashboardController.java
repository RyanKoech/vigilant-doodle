package com.example.vigilantdoodle;

import com.example.vigilantdoodle.datamodels.AdminReports;
import com.example.vigilantdoodle.datamodels.PoliceReports;
import com.example.vigilantdoodle.utilities.Data;
import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
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
    private JFXTreeTableView<AdminReports> dashboardTableView;

    @FXML
    private TreeTableColumn<AdminReports, String> obIdTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String> policeNameTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String> offenderNameTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String> locationTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String> dateTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String>timeTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String> crimeTableColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWelcomeBannerLabel();
        showAdminReports();
        getFirstDashboardFacts();
        getSecondDashboardFacts();
        getThirdDashboardFacts();
    }

    //Side Menu Navigation Button Actions
    //To Dashboard Tab
    @FXML
    private void onShowDashboardTab(ActionEvent event) { dashboardTabVBox.toFront(); }
    //To Reporting Tab
    @FXML
    private void onShowReportingTab(ActionEvent event) { reportingTabVBox.toFront(); }
    //To Custodies Tab
    @FXML
    private void onShowCustodiesTab(ActionEvent event) { custodiesTabVBox.toFront(); }
    //To Add Police Tab
    @FXML
    private void onShowAddPoliceTab(ActionEvent event){ addPoliceTabVBox.toFront(); }
    //Edit Police Tab
    @FXML
    private void onShowEditPoliceTab(ActionEvent event){ editPoliceTabVBox.toFront(); }

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

    //Create Observable list of type PoliceReports (From Database) to be used in TableView
    private ObservableList<AdminReports> getAdminReportList() {
        ObservableList<AdminReports> adminReportsList = FXCollections.observableArrayList();

        Connection connection = MysqlConnector.connectDB();

        try {
            PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT cases.OB_id, users.Name Police_Name, Offenders.Name Offender_Name, cases.Location, cases.Date, cases.Time, crime_types.Type Crime_Type FROM cases INNER JOIN users ON cases.Police_Id = users.Police_Id INNER JOIN citizens as Offenders ON cases.Offender_Id = Offenders.`National ID` INNER JOIN `crime types` as crime_types ON cases.Crime_Type = crime_types.Type_Id ");
            //st.setString(1, Data.POLICE_ID);
            ResultSet res = st.executeQuery();
            AdminReports adminReports;

            while (res.next()) {
                adminReports = new AdminReports(res.getString("OB_id"), res.getString("Police_Name"), res.getString("Offender_Name"), res.getString("Location"), res.getString("Date"), res.getString("Time"), res.getString("Crime_Type"));
                adminReportsList.add(adminReports);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return adminReportsList;
    }

    //Display contents of an Observable list into TableView
    private void showAdminReports() {
        ObservableList<AdminReports> list = getAdminReportList();

        obIdTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("obId"));
        policeNameTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("policeName"));
        offenderNameTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("offenderName"));
        locationTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("location"));
        dateTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("date"));
        timeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("time"));
        crimeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<AdminReports, String>("crime"));

        TreeItem<AdminReports> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        dashboardTableView.setRoot(root);
        dashboardTableView.setShowRoot(false);
    }

    private void getFirstDashboardFacts(){

        Connection connection = MysqlConnector.connectDB();

        try {
            PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT COUNT(cases.OB_id) total FROM `cases`");
            ResultSet res = st.executeQuery();

            if (res.next()) {
                fact1Label.setText("Total Cases");
                stat1Label.setText(res.getString("total") + " cases");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getSecondDashboardFacts(){

        Connection connection = MysqlConnector.connectDB();

        try {
            PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT `crime types`.`Type`, COUNT(`crime types`.`Type`) frequencies FROM `cases` INNER JOIN `crime types` ON cases.Crime_Type = `crime types`.`Type_Id` GROUP BY `crime types`.`Type` ORDER BY frequencies DESC  LIMIT 1");
            ResultSet res = st.executeQuery();

            if (res.next()) {
                fact2Label.setText(res.getString("Type"));
                stat2Label.setText(res.getString("frequencies") + " cases");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getThirdDashboardFacts(){

        Connection connection = MysqlConnector.connectDB();

        try {
            PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT SUM(bails.Amount) bails FROM `bails` ");
            ResultSet res = st.executeQuery();

            if (res.next()) {
                fact3Label.setText("Total Bails");
                stat3Label.setText("Ksh " + res.getString("bails"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
