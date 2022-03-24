package com.example.vigilantdoodle;

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
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PoliceDashboardController implements Initializable {

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
    private JFXTreeTableView<PoliceReports> dashboardTableView;

    @FXML
    private TreeTableColumn<PoliceReports, String> obIdTableColumn;

    @FXML
    private TreeTableColumn<PoliceReports, String> reporterNameTableColumn;

    @FXML
    private TreeTableColumn<PoliceReports, String> offenderNameTableColumn;

    @FXML
    private TreeTableColumn<PoliceReports, String> locationTableColumn;

    @FXML
    private TreeTableColumn<PoliceReports, String> dateTableColumn;

    @FXML
    private TreeTableColumn<PoliceReports, String> timeTableColumn;

    @FXML
    private TreeTableColumn<PoliceReports, String> crimeTypeTableColumn;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWelcomeBannerLabel();
        showPoliceReports();
    }

    //Logout Button Function
    @FXML
    private void onLogout(ActionEvent event) {
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
    private void onShowCustodiesTab(ActionEvent event) {
        custodiesTabVBox.toFront();
    }

    //No 2
    @FXML
    private void onShowDashboardTab(ActionEvent event) {
        dashboardTabVBox.toFront();
    }

    //No 3
    @FXML
    private void onShowReportingTab(ActionEvent event) {
        reportingTabVBox.toFront();
    }

    @FXML
    private void onUpdateCustody(ActionEvent event) {

    }

    @FXML
    private void onSearchCase(ActionEvent event) {

    }

    @FXML
    private void onReportCrime(ActionEvent event){

    }

    //Sets the logged in police name on the welcome banner
    private void setWelcomeBannerLabel(){
        Connection dbConn = MysqlConnector.connectDB();
        if(dbConn != null){
            try {

                PreparedStatement st = (PreparedStatement)dbConn.prepareStatement ("SELECT Name FROM `users` WHERE `Police_Id` = ?");
                st.setString(1, Data.POLICE_ID);
                ResultSet res = st.executeQuery();

                while (res.next()){
                    String welcomeName = res.getString("Name");
                    welcomeBannerLabel.setText("Inspector "+ welcomeName);

                }
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("The connection is not available");
        }
    }

    private ObservableList<PoliceReports> getPoliceReportList(){
        ObservableList<PoliceReports> policeReportsList = FXCollections.observableArrayList();

        Connection connection = MysqlConnector.connectDB();
//        String query = "SELECT `OB_id`, `Reporter_Id`, `Offender_Id`, `Location`, `Date`, `Time`, `Crime_Type` FROM `cases` WHERE `Police_Id`=?";
//        Statement st;
//        ResultSet rs;

        try {
//            st = connection.createStatement();
            PreparedStatement st = (PreparedStatement)connection.prepareStatement ("SELECT `OB_id`, `Reporter_Id`, `Offender_Id`, `Location`, `Date`, `Time`, `Crime_Type` FROM `cases` WHERE `Police_Id`=?");
            st.setString(1, Data.POLICE_ID);
            ResultSet res =st.executeQuery();
            PoliceReports policeReports;

            while(res.next()){
                policeReports = new PoliceReports(res.getString("OB_id"), res.getString("Reporter_Id"), res.getString("Offender_Id") , res.getString("Location")  , res.getString("Date") , res.getString("Time") , res.getString("Crime_Type"));
                policeReportsList.add(policeReports);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return policeReportsList;
    }

    private void showPoliceReports()  {
        ObservableList<PoliceReports> list = getPoliceReportList();

        obIdTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("obId"));
        reporterNameTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("reporterName"));
        offenderNameTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("offenderName"));
        locationTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("location"));
        dateTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("date"));
        timeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("time"));
        crimeTypeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<PoliceReports, String>("crime"));

        TreeItem<PoliceReports> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        dashboardTableView.setRoot(root);
        dashboardTableView.setShowRoot(false);
    }

}
