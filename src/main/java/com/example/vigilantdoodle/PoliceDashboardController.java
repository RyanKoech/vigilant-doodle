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
import java.util.*;
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
    private ChoiceBox<String> custodyTypeChoiceBox;

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
    private ChoiceBox<String> crimeTypeChoiceBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private JFXButton reportButton;

    private final List<TextField> reportingTextFieldList = new ArrayList<>();

    private final Map crimeTypetoCrimeIdMap = new HashMap();

    private final Map custodyTypetoCustodyIdMap = new HashMap();

    private String offenderId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWelcomeBannerLabel();
        showPoliceReports();
        createTextFieldList();
        setCrimeTypeChoiceBoxItems();
        setCustodyTypeChoiceBoxItems();
        updateCustodyButton.setDisable(true);
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
        if(isCustodiesChoiceBoxValueEmpty()){
            return;
        }
        updateCustody(offenderId);
    }

    @FXML
    private void onSearchCase(ActionEvent event) {
        if(isObIdTextFieldEmpty()){
            return;
        }
        getSuspectCustodyRecords(obNumberTextField.getText());
    }

    @FXML
    private void onReportCrime(ActionEvent event) {
        if (areReportingTextFieldsEmpty() || isChoiceBoxValueEmpty() || isReportingTextAreaEmpty()) {
            System.out.println("All Fields Must be Filled");
            return;
        }

        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("INSERT INTO `cases` (`OB_id`, `Police_Id`, `Reporter_Id`, `Offender_Id`, `Location`, `Date`, `Time`, `Description`, `Crime_Type`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, Data.POLICE_ID);
                statement.setString(2, reporterIdTextField.getText());
                statement.setString(3, offenderNameTextField.getText());
                statement.setString(4, locationTextField.getText());
                statement.setString(5, dateTextField.getText());
                statement.setString(6, timeTextField.getText());
                statement.setString(7, descriptionTextArea.getText());
                statement.setString(8, crimeTypetoCrimeIdMap.get(crimeTypeChoiceBox.getValue()).toString());

                int res = statement.executeUpdate();

                resetReportingTabInputs();
                //PopUpaAlert.display("SUCCESS", "Evidence Successfully Updated.");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
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
    private ObservableList<PoliceReports> getPoliceReportList() {
        ObservableList<PoliceReports> policeReportsList = FXCollections.observableArrayList();

        Connection connection = MysqlConnector.connectDB();

        try {
            PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT cases.OB_id, Reporters.Name Reporter_Name, Offenders.Name Offender_Name, " +
                    "cases.Location, cases.Date, cases.Time, crime_types.Type Crime_Type FROM cases INNER JOIN citizens as Reporters ON cases.Reporter_Id = Reporters.`National ID`" +
                    " INNER JOIN citizens as Offenders ON cases.Offender_Id = Offenders.`National ID` INNER JOIN `crime types` as crime_types ON cases.Crime_Type = crime_types.Type_Id WHERE `Police_Id`=?");
            st.setString(1, Data.POLICE_ID);
            ResultSet res = st.executeQuery();
            PoliceReports policeReports;

            while (res.next()) {
                policeReports = new PoliceReports(res.getString("OB_id"), res.getString("Reporter_Name"), res.getString("Offender_Name"), res.getString("Location"), res.getString("Date"), res.getString("Time"), res.getString("Crime_Type"));
                policeReportsList.add(policeReports);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return policeReportsList;
    }

    //Display contents of an Observable list into TableView
    private void showPoliceReports() {
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

    //Build Reporting TextFeild List
    private void createTextFieldList() {
        reportingTextFieldList.add(reporterNameTextField);
        reportingTextFieldList.add(reporterIdTextField);
        reportingTextFieldList.add(offenderNameTextField);
        reportingTextFieldList.add(locationTextField);
        reportingTextFieldList.add(dateTextField);
        reportingTextFieldList.add(timeTextField);
    }

    //Check if reporting Textfields are empty(True = are Empty)
    private Boolean areReportingTextFieldsEmpty() {
        for (TextField reportingTextField : reportingTextFieldList) {
            if (reportingTextField.getText() == null || reportingTextField.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //Check if reporting case description TextField is empty
    private Boolean isReportingTextAreaEmpty() {
        return descriptionTextArea.getText() == null || descriptionTextArea.getText().trim().isEmpty();
    }

    //Check if reporting case description ChoiceBox is empty
    private Boolean isChoiceBoxValueEmpty() {
        return (crimeTypeChoiceBox.getValue() == null);
    }

    //Check if Ob_Id TextField is empty
    private Boolean isObIdTextFieldEmpty(){
        return (obNumberTextField.getText() == null || obNumberTextField.getText().trim().isEmpty());
    }

    //Check if custodies choice box value is empty
    private  Boolean isCustodiesChoiceBoxValueEmpty(){
        return (custodyTypeChoiceBox.getValue() == null);
    }

    //Sets ChoiceBox Items from the Database and Maps Crime types to  Type Id
    private void setCrimeTypeChoiceBoxItems() {
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("SELECT * FROM `crime types`");
                ResultSet resultSet = statement.executeQuery();

                //Clear Map
                crimeTypetoCrimeIdMap.clear();

                while (resultSet.next()) {
                    String crime = resultSet.getString("Type");
                    String crimeId = resultSet.getString("Type_Id");

                    //Map crime type to crime id
                    crimeTypetoCrimeIdMap.put(crime, crimeId);

                    //Add crime type to ChoiceBox
                    crimeTypeChoiceBox.getItems().addAll(crime);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Sets ChoiceBox Items from the Database and Maps Crime types to  Type Id
    private void setCustodyTypeChoiceBoxItems() {
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("SELECT `Type_Id`, `Custody_Type` FROM `custody types`");
                ResultSet resultSet = statement.executeQuery();

                //Clear Map
                custodyTypetoCustodyIdMap.clear();

                while (resultSet.next()) {
                    String custody = resultSet.getString("Custody_Type");
                    String custodyId = resultSet.getString("Type_Id");

                    //Map crime type to crime id
                    custodyTypetoCustodyIdMap.put(custody, custodyId);

                    //Add crime type to ChoiceBox
                    custodyTypeChoiceBox.getItems().addAll(custody);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Reset the Reporting Tab Inputs
    private void resetReportingTabInputs() {
        for (TextField reportingTextField : reportingTextFieldList) {
            reportingTextField.setText("");
        }
        descriptionTextArea.setText("");
        crimeTypeChoiceBox.setValue(null);
    }

    private void getSuspectCustodyRecords(String obNumber) {
        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT cases.Offender_Id, citizens.Name, `custody types`.`Custody_Type`, `custody types`.`bail_index` FROM cases INNER JOIN citizens ON cases.Offender_Id = citizens.`National ID` INNER JOIN offenders ON cases.Offender_Id = offenders.National_Id INNER JOIN `custody types` ON offenders.Custody_Id = `custody types`.`Type_Id` WHERE cases.OB_id = ?");
                st.setString(1, obNumber);
                ResultSet res = st.executeQuery();

                if (res.next()) {

                    double bailIndex = Double.parseDouble(res.getString("bail_index"));
                    suspectNameLabel.setText(res.getString("Name"));
                    currentCustodyLabel.setText(res.getString("Custody_Type"));
                    bailFeeLabel.setText(String.valueOf(bailIndex*Data.BASE_BAIL));
                    offenderId = res.getString("Offender_Id");
                }
                //To enable the user to be able to click in the update button
                updateCustodyButton.setDisable(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateCustody(String offenderId){
        Connection connection = MysqlConnector.connectDB();
        if(connection != null){
            try {

                PreparedStatement st = (PreparedStatement)connection.prepareStatement ("UPDATE `offenders` SET `Custody_Id` = ? WHERE `offenders`.`National_Id` = ?");
                st.setString(1, custodyTypetoCustodyIdMap.get(custodyTypeChoiceBox.getValue()).toString());
                st.setString(2, offenderId);
                int res = st.executeUpdate();

                System.out.println("Success!");
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("The connection is not available");
        }
    }

}
