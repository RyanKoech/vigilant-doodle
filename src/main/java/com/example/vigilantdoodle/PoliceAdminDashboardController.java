package com.example.vigilantdoodle;

import com.example.vigilantdoodle.datamodels.AdminReports;
import com.example.vigilantdoodle.ui_utilities.ConfirmBox;
import com.example.vigilantdoodle.ui_utilities.PopUpAlert;
import com.example.vigilantdoodle.utilities.Data;
import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.example.vigilantdoodle.utilities.SendEmail;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

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
    private DatePicker dateDatePicker;

    @FXML
    private TextField timeTextField;

    @FXML
    private ChoiceBox<String> crimeTypeChoiceBox;

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
    private ChoiceBox<String> custodyTypeChoiceBox;

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
    private TextField addPoliceNationalIdTextField;

    @FXML
    private TextField addPolicePasswordTextField;

    @FXML
    private ChoiceBox<String> addPolicePoliceRoleChoiceBox;

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
    private TextField editPoliceNationalIdTextField1;

    @FXML
    private TextField editPolicePoliceNameTextField;

    @FXML
    private TextField editPolicePoliceNumberTextField;

    @FXML
    private TextField editPoliceEmailAddressTextField;

    @FXML
    private PasswordField editPolicePasswordTextField1;

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
    private TreeTableColumn<AdminReports, String> timeTableColumn;

    @FXML
    private TreeTableColumn<AdminReports, String> crimeTableColumn;

    @FXML
    private VBox statisticsTabVBox;

    @FXML
    private LineChart<String, Integer> monthlyCasesLineChart;

    @FXML
    private BarChart<String, Integer> crimeTypesBarGraph;

    //NON FXML PROPERTIES
    private final Map crimeTypetoCrimeIdMap = new HashMap();

    private final Map custodyTypetoCustodyIdMap = new HashMap();

    private final List<TextField> reportingTextFieldList = new ArrayList<>();

    private final List<TextField> addPoliceTextFieldList = new ArrayList<>();

    private final List<TextField> editPoliceTextFieldList = new ArrayList<>();

    private final EnumMap<policeRoleEnum, String> policeRoleMapping = new EnumMap<>(policeRoleEnum.class);

    private String offenderId;

    private enum policeRoleEnum {
        POLICE, ADMIN
    }

    //METHODS
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWelcomeBannerLabel();
        showAdminReports();
        getFirstDashboardFacts();
        getSecondDashboardFacts();
        getThirdDashboardFacts();
        setCrimeTypeChoiceBoxItems();
        createTextFieldList();
        setCustodyTypeChoiceBoxItems();
        setPoliceRoleChoiceBoxItems();
        createPoliceRoleMapping();
        createAddPoliceTextFieldList();
        createEditPoliceTextFieldList();
        populateMonthlyCasesLineGraph();
        setDatePickerFormat();
        populateCrimeTypesBarGraph();
    }

    //Side Menu Navigation Button Actions
    //To Dashboard Tab
    @FXML
    private void onShowDashboardTab(ActionEvent event) {
        dashboardTabVBox.toFront();
    }

    //To Reporting Tab
    @FXML
    private void onShowReportingTab(ActionEvent event) {
        reportingTabVBox.toFront();
    }

    //To Custodies Tab
    @FXML
    private void onShowCustodiesTab(ActionEvent event) {
        custodiesTabVBox.toFront();
    }

    //To Add Police Tab
    @FXML
    private void onShowAddPoliceTab(ActionEvent event) {
        addPoliceTabVBox.toFront();
    }

    //Edit Police Tab
    @FXML
    private void onShowEditPoliceTab(ActionEvent event) {
        editPoliceTabVBox.toFront();
    }

    //Statistics Tab
    @FXML
    private void onShowStatisticsTab(ActionEvent event) {
        statisticsTabVBox.toFront();
    }

    //Logout Button Function
    @FXML
    private void onLogout(ActionEvent event) {
        boolean confirm = ConfirmBox.displayConfirmBox(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Are you sure you want to logout?");
        if (!confirm) return;
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

        if (areTextFieldsEmpty(addPoliceTextFieldList) || isAddPoliceChoiceBoxValueEmpty()) {
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        boolean confirm = ConfirmBox.displayConfirmBox(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Add new police to the system?");
        if (!confirm) return;
        uploadNewPoliceInformation();
        uploadNewPoliceLoginInformation();
        resetAddPoliceInputFields();

    }

    @FXML
    void onReportCrime(ActionEvent event) {

        if (areTextFieldsEmpty(reportingTextFieldList) || isReportingChoiceBoxValueEmpty() || isReportingTextAreaEmpty() || isDatePickerValueEmpty()) {
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }

        String investigatorId = getLeastOccupiedPolice();
        String obId = "";

        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("INSERT INTO `cases` (`OB_id`, `Police_Id`, `Reporter_Id`, `Offender_Id`, `Location`, `Date`, `Time`, `Description`, `Crime_Type`, `Investigator_Id`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?); SELECT LAST_INSERT_ID() AS id;");
                statement.setString(1, Data.POLICE_ID);
                statement.setString(2, reporterIdTextField.getText());
                statement.setString(3, offenderIdTextField.getText());
                statement.setString(4, locationTextField.getText());
                statement.setString(5, dateDatePicker.getValue().toString());
                statement.setString(6, timeTextField.getText());
                statement.setString(7, descriptionTextArea.getText());
                statement.setString(8, crimeTypetoCrimeIdMap.get(crimeTypeChoiceBox.getValue()).toString());
                statement.setString(9, investigatorId);

                boolean hasMoreResultSets = statement.execute();

                READING_QUERY_RESULTS: // label
                while ( hasMoreResultSets || statement.getUpdateCount() != -1 ) {
                    if ( hasMoreResultSets ) {
                        ResultSet resultSet = statement.getResultSet();
                        if(resultSet.next()){
                            obId = (resultSet.getString("id"));
                        }
                    }
                    else {
                        int queryResult = statement.getUpdateCount();
                        if ( queryResult == -1 ) { // no more queries processed
                            break READING_QUERY_RESULTS;
                        }
                        // handle success, failure, generated keys, etc here
                    }

                    // check to continue in the loop
                    hasMoreResultSets = statement.getMoreResults();
                }

                sendReporterEmail(obId);
                sendInvestigatorEmail(investigatorId, obId);
                resetReportingTabInputs();
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.SUCCESS), "Crime Reported Successfully");
            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }

    }

    @FXML
    void onSearchCase(ActionEvent event) {
        if (isObIdTextFieldEmpty()) {
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        getSuspectCustodyRecords(obNumberTextField.getText());
    }

    @FXML
    void onUpdateCustody(ActionEvent event) {
        if (isCustodiesChoiceBoxValueEmpty()) {
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        updateCustody(offenderId);
    }

    @FXML
    void onSearchPolice(ActionEvent event) {
        if(seachPoliceTextField.getText().isEmpty()){
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        getPoliceRecords(seachPoliceTextField.getText());
    }

    @FXML
    void onUpdatePolice(ActionEvent event) {
        if(areTextFieldsEmpty(editPoliceTextFieldList)){
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        updatePoliceInformation();
        if(!editPolicePasswordTextField1.getText().isEmpty()){
           updatePolicePassword();
        }
        resetEditPoliceInputFields();
    }
    @FXML
    void onDeletePolice(ActionEvent event) {
        if(areTextFieldsEmpty(editPoliceTextFieldList)){
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        deletePolice();
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

    private void getFirstDashboardFacts() {

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

    private void getSecondDashboardFacts() {

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

    private void getThirdDashboardFacts() {

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

    //Build Reporting TextFeild List
    private void createTextFieldList() {
        reportingTextFieldList.add(reporterNameTextField);
        reportingTextFieldList.add(reporterIdTextField);
        reportingTextFieldList.add(offenderIdTextField);
        reportingTextFieldList.add(locationTextField);
        reportingTextFieldList.add(timeTextField);
    }

    //Check if reporting Textfields are empty(True = are Empty)
    private Boolean areTextFieldsEmpty(List<TextField> textFieldList) {
        for (TextField textField : textFieldList) {
            if (textField.getText() == null || textField.getText().trim().isEmpty()) {
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
    private Boolean isReportingChoiceBoxValueEmpty() {
        return (crimeTypeChoiceBox.getValue() == null);
    }

    private Boolean isDatePickerValueEmpty(){
        return dateDatePicker.getValue() == null;
    }

    //Reset the Reporting Tab Inputs
    private void resetReportingTabInputs() {
        for (TextField reportingTextField : reportingTextFieldList) {
            reportingTextField.setText("");
        }
        descriptionTextArea.setText("");
        crimeTypeChoiceBox.setValue(null);
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

    //Check if Ob_Id TextField is empty
    private Boolean isObIdTextFieldEmpty() {
        return (obNumberTextField.getText() == null || obNumberTextField.getText().trim().isEmpty());
    }

    //Fetches and Loads Suspect's Records from the Database to the View
    private void getSuspectCustodyRecords(String obNumber) {
        Connection connection = MysqlConnector.connectDB();

        if (connection != null) {
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT cases.Offender_Id, citizens.Name, `custody types`.`Custody_Type`, `custody types`.`bail_index` FROM cases INNER JOIN citizens ON cases.Offender_Id = citizens.`National ID` INNER JOIN offenders ON cases.Offender_Id = offenders.National_Id INNER JOIN `custody types` ON offenders.Custody_Id = `custody types`.`Type_Id` WHERE cases.OB_id = ?");
                st.setString(1, obNumber);
                ResultSet res = st.executeQuery();

                if (res.next()) {

                    double bailIndex = Double.parseDouble(res.getString("bail_index"));
                    suspectNameLabel.setText(res.getString("Name"));
                    currentCustodyLabel.setText(res.getString("Custody_Type"));
                    bailFeeLabel.setText(String.valueOf(bailIndex * Data.BASE_BAIL));
                    offenderId = res.getString("Offender_Id");
                }
                //To enable the user to be able to click in the update button
                updateCustodyButton.setDisable(false);
            } catch (Exception ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    //Check if custodies choice box value is empty
    private Boolean isCustodiesChoiceBoxValueEmpty() {
        return (custodyTypeChoiceBox.getValue() == null);
    }

    //Updates Suspect Custody Status into the Database
    private void updateCustody(String offenderId) {
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {

                PreparedStatement st = (PreparedStatement) connection.prepareStatement("UPDATE `offenders` SET `Custody_Id` = ? WHERE `offenders`.`National_Id` = ?");
                st.setString(1, custodyTypetoCustodyIdMap.get(custodyTypeChoiceBox.getValue()).toString());
                st.setString(2, offenderId);
                int res = st.executeUpdate();

            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

    //Build Reporting TextFeild List
    private void createAddPoliceTextFieldList() {
        addPoliceTextFieldList.add(addPolicePoliceIdTextField);
        addPoliceTextFieldList.add(addPolicePoliceNameTextField);
        addPoliceTextFieldList.add(addPoliceNationalIdTextField);
        addPoliceTextFieldList.add(addPolicePhoneNumberTextField);
        addPoliceTextFieldList.add(addPoliceEmailAddressTextField);
        addPoliceTextFieldList.add(addPolicePasswordTextField);
    }

    //Check if add police case description ChoiceBox is empty
    private Boolean isAddPoliceChoiceBoxValueEmpty() {
        return (addPolicePoliceRoleChoiceBox.getValue() == null);
    }

    //Uploads new police information into the database
    private void uploadNewPoliceInformation() {
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("INSERT INTO `users` (`Police_Id`, `Name`, `National_Id`, `Phone_Number`, `Occupied`, `Email_Address`) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, addPolicePoliceIdTextField.getText());
                statement.setString(2, addPolicePoliceNameTextField.getText());
                statement.setString(3, addPoliceNationalIdTextField.getText());
                statement.setString(4, addPolicePhoneNumberTextField.getText());
                statement.setString(5, "test");
                statement.setString(6, addPoliceEmailAddressTextField.getText());

                int res = statement.executeUpdate();

            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

    //Sets ChoiceBox Items from the Database and Maps Crime types to  Type Id
    private void setPoliceRoleChoiceBoxItems() {
        addPolicePoliceRoleChoiceBox.getItems().removeAll();
        addPolicePoliceRoleChoiceBox.getItems().addAll("Police", "Admin Police");
    }

    //Create police roles mapping
    private void createPoliceRoleMapping(){
        policeRoleMapping.put(policeRoleEnum.ADMIN, "Admin Police");
        policeRoleMapping.put(policeRoleEnum.POLICE, "Police");
    }

    //Uploads new police information into the database
    private void uploadNewPoliceLoginInformation() {
        String query = "INSERT INTO `" + ((policeRoleMapping.get(policeRoleEnum.ADMIN)).equals(addPolicePoliceRoleChoiceBox.getValue()) ? "police admin": "police") +"` (`Police_Id`, `Password`) VALUES (?, ?) ";

        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1, addPolicePoliceIdTextField.getText());
                statement.setString(2, addPolicePasswordTextField.getText());

                int res = statement.executeUpdate();

                //PopUpaAlert.display("SUCCESS", "Evidence Successfully Updated.");
            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

    //Resets Add Police Tap Input Fields
    private void resetAddPoliceInputFields() {
        for (TextField addPoliceTextField : addPoliceTextFieldList) {
            addPoliceTextField.setText("");
        }
        addPolicePoliceRoleChoiceBox.setValue(null);
    }

    private void getPoliceRecords(String policeId) {
        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT police.Police_Id, users.National_Id, users.Name, users.Phone_Number, users.Email_Address FROM `police` INNER JOIN users ON police.Police_Id = users.Police_Id WHERE police.Police_Id = ?");
                st.setString(1, policeId);
                ResultSet res = st.executeQuery();

                if (res.next()) {
                    editPolicePoliceIdTextField.setText(res.getString("Police_Id"));
                    editPoliceNationalIdTextField1.setText(res.getString("National_Id"));
                    editPolicePoliceNameTextField.setText(res.getString("Name"));
                    editPolicePoliceNumberTextField.setText(res.getString("Phone_Number"));
                    editPoliceEmailAddressTextField.setText(res.getString("Email_Address"));

                    //To enable the user to be able to click in the buttons
                    updatePoliceButton.setDisable(false);
                    deletePoliceButton.setDisable(false);
                }
            } catch (Exception ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void createEditPoliceTextFieldList() {
        editPoliceTextFieldList.add(editPolicePoliceIdTextField);
        editPoliceTextFieldList.add(editPolicePoliceNameTextField);
        editPoliceTextFieldList.add(editPoliceNationalIdTextField1);
        editPoliceTextFieldList.add(editPolicePoliceNumberTextField);
        editPoliceTextFieldList.add(editPoliceEmailAddressTextField);
    }

    private void updatePoliceInformation(){
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {

                PreparedStatement st = (PreparedStatement) connection.prepareStatement("UPDATE `users` SET `Name`=?,`National_Id`=?,`Phone_Number`=?,`Email_Address`=? WHERE `Police_Id`=?");
                st.setString(1, editPolicePoliceNameTextField.getText());
                st.setString(2, editPoliceNationalIdTextField1.getText());
                st.setString(3, editPolicePoliceNumberTextField.getText());
                st.setString(4, editPoliceEmailAddressTextField.getText());
                st.setString(5, editPolicePoliceIdTextField.getText());

                int res = st.executeUpdate();
            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

    private void deletePolice(){
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {

                PreparedStatement st = (PreparedStatement) connection.prepareStatement("UPDATE `police` SET `Password` = '', `Available` = 'false' WHERE `police`.`Police_Id` = ?");
                st.setString(1, editPolicePoliceIdTextField.getText());

                int res = st.executeUpdate();
            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

    private void updatePolicePassword(){
        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {

                PreparedStatement st = (PreparedStatement) connection.prepareStatement("UPDATE `police` SET `Password`=? WHERE `Police_Id`=?");
                st.setString(1, editPolicePasswordTextField1.getText());
                st.setString(2, editPolicePoliceIdTextField.getText());

                int res = st.executeUpdate();

            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }

    private void resetEditPoliceInputFields(){
        for (TextField addPoliceTextField : editPoliceTextFieldList) {
            addPoliceTextField.setText("");
        }
        editPolicePasswordTextField1.setText("");
    }

    private void sendReporterEmail(String obNumber){

        HashMap<Data.emailInfo, String> emailInfoMap = new HashMap ();
        String recipient, recipientName, reportTime, reportDate;

        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT `citizens`.`Email`, `citizens`.`Name`, `cases`.`Date`, `cases`.`Time` FROM `cases` INNER JOIN `citizens` ON `cases`.`Reporter_Id` = `citizens`.`National ID` WHERE `OB_id`=? ");
                st.setString(1, obNumber);
                ResultSet res = st.executeQuery();

                if (res.next()) {
                    recipient = res.getString("Email");
                    recipientName = res.getString("Name");
                    reportTime = res.getString("Time");
                    reportDate = res.getString("Date");

                    //Prepare Email Information
                    emailInfoMap.put(Data.emailInfo.RECIPIENT, recipient);
                    emailInfoMap.put(Data.emailInfo.EMAIL_SUBJECT, Data.getReporterEmailSubject());
                    emailInfoMap.put(Data.emailInfo.EMAIL_BODY, Data.getReporterEmailBody(recipientName, reportDate, reportTime, obNumber));
                    emailInfoMap.put(Data.emailInfo.EMAIL_TYPE, Data.EMAIL_TYPES.CASE_CONFIRMATION.toString());

                    SendEmail.notification(emailInfoMap);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getLeastOccupiedPolice(){
        String policeId = "";

        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT `police`.`Police_Id`, COUNT(`cases`.`Investigator_Id`) as cases FROM `cases` RIGHT JOIN `police` ON `cases`.`Investigator_Id` = `police`.`Police_Id` WHERE `police`.`Available` LIKE 'true' GROUP BY `police`.`Police_Id` ORDER BY cases ASC  LIMIT 1");
                ResultSet res = st.executeQuery();

                if (res.next()) {
                    policeId = res.getString("Police_Id");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return policeId;
    }

    private void sendInvestigatorEmail(String investigatorId, String obNumber){
        HashMap<Data.emailInfo, String> emailInfoMap = new HashMap ();
        String recipient, investigatorName;

        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT `users`.`Name` , `users`.`Email_Address` FROM `users` WHERE `users`.`Police_Id` = ?");
                st.setString(1, investigatorId);
                ResultSet res = st.executeQuery();

                if (res.next()) {
                    recipient = res.getString("Email_Address");
                    investigatorName = res.getString("Name");

                    //Prepare Email Information
                    emailInfoMap.put(Data.emailInfo.RECIPIENT, recipient);
                    emailInfoMap.put(Data.emailInfo.EMAIL_SUBJECT, Data.getInvestigatorEmailSubject());
                    emailInfoMap.put(Data.emailInfo.EMAIL_BODY, Data.getInvestigatorEmailBody(investigatorName, obNumber));
                    emailInfoMap.put(Data.emailInfo.EMAIL_TYPE, Data.EMAIL_TYPES.NEW_CASE_NOTIFICATION.toString());

                    SendEmail.notification(emailInfoMap);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void populateMonthlyCasesLineGraph(){
        XYChart.Series series = new XYChart.Series();
        series.setName("Monthly Cases");

        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT YEAR(`Date`) Year,MONTH(`Date`) Month,COUNT(`OB_id`) Total_Cases FROM `cases` GROUP BY YEAR(`Date`),MONTH(`Date`) ORDER BY YEAR(`Date`) DESC,MONTH(`Date`) DESC ");
                ResultSet res = st.executeQuery();

                if (res.next()) {
                    int count = 5;
                    boolean innerWhileRan = false;
                    int year = parseInt(res.getString("Year"));
                    int month = parseInt(res.getString("Month"));
                    int  totalCases = parseInt(res.getString("Total_Cases"));
                    series.getData().add(new XYChart.Data(year + " " + Data.MONTHS[month-1], totalCases));

                    while(res.next() && count > 0){
                        innerWhileRan = false;
                        month--;
                        if(month == 0){
                            month = 12;
                            year--;
                        }
                        while (parseInt(res.getString("Month")) != month && count > 0){
                            innerWhileRan = true;
                            series.getData().add(new XYChart.Data(year + " " + Data.MONTHS[month-1], 0));
                            month--;
                            if(month == 0){
                                month = 12;
                                year--;
                            }
                            if(!(parseInt(res.getString("Month")) != month && count > 0)) {
                                innerWhileRan = false;
                            }
                            count--;

                        }
                        if(count > 0) {
                            year = parseInt(res.getString("Year"));
                            month = parseInt(res.getString("Month"));
                            totalCases = parseInt(res.getString("Total_Cases"));
                            series.getData().add(new XYChart.Data(year + " " + Data.MONTHS[month-1], totalCases));
                        }
                        if(!innerWhileRan) count--;
                    }
                    monthlyCasesLineChart.getData().add(series);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void populateCrimeTypesBarGraph(){
        XYChart.Series series = new XYChart.Series();
        series.setName("Totals of Crime Types");

        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement(" SELECT COUNT(`cases`.`OB_id`) totals, `crime types`.`Type` FROM `cases` JOIN `crime types` ON `cases`.`Crime_Type` = `crime types`.`Type_Id` where `cases`.`Date` > curdate() - interval (dayofmonth(curdate()) - 1) day - interval 6 month GROUP BY `cases`.`Crime_Type` ORDER BY YEAR(`Date`) DESC,MONTH(`Date`) DESC ");
                ResultSet res = st.executeQuery();

                while (res.next()) {
                    String crimeType = res.getString("Type");
                    int totals = parseInt(res.getString("totals"));
                    series.getData().add(new XYChart.Data(crimeType, totals));
                }

                crimeTypesBarGraph.getData().add(series);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setDatePickerFormat(){
        dateDatePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dateDatePicker.setPromptText(pattern.toLowerCase());
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        restrictDatePicker(dateDatePicker, LocalDate.of(1900, Month.JANUARY, 1), LocalDate.now());
    }

    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.focusedProperty().addListener((obs, oldVal, newVal) ->{

            LocalDate enteredDate = datePicker.getValue();
            LocalDate dateTime = LocalDate.now();
            String enteredDateString = datePicker.getEditor().textProperty().get();
            System.out.println(enteredDate);

            DATE_PARSE_ERROR_CATCH:
            if(!newVal && !enteredDateString.isEmpty()){
                try{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    dateTime = LocalDate.parse(enteredDateString, formatter);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), "Invalid Date Entered. It is be reset to to: " + enteredDate);
                    datePicker.getEditor().textProperty().setValue(enteredDate.toString());
                    newVal = true;
                    break DATE_PARSE_ERROR_CATCH;
                }
                if (dateTime.isBefore(minDate)) {
                    datePicker.getEditor().textProperty().setValue(minDate.toString());
                    datePicker.setValue(maxDate);
                    PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), "Invalid Date Entered. It is be reset to to: " + minDate);
                } else if (dateTime.isAfter(maxDate)) {
                    datePicker.getEditor().textProperty().setValue(maxDate.toString());
                    datePicker.setValue(minDate);
                    PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), "Invalid Date Entered. It is be reset to to: " + maxDate);
                }else {
                    datePicker.setValue(dateTime);
                    System.out.println("Date has been updated to: " + datePicker.getValue());
                }
            }else if(enteredDateString.isEmpty()){
                datePicker.setValue(null);
                System.out.println("Date has been updated to: " + datePicker.getValue());
            }
        });
    }
}