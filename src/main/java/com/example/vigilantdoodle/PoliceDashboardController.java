package com.example.vigilantdoodle;

import com.example.vigilantdoodle.datamodels.PoliceReports;
import com.example.vigilantdoodle.ui_utilities.ConfirmBox;
import com.example.vigilantdoodle.ui_utilities.PopUpAlert;
import com.example.vigilantdoodle.utilities.Data;
import com.example.vigilantdoodle.utilities.Data.emailInfo;
import com.example.vigilantdoodle.utilities.MysqlConnector;
import com.example.vigilantdoodle.utilities.SendEmail;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.json.JSONObject;
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
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
    private TextField timeTextField;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private ChoiceBox<String> crimeTypeChoiceBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private JFXButton reportButton;

    private final List<TextField> reportingTextFieldList = new ArrayList<>();

    private final Map crimeTypetoCrimeIdMap = new HashMap();

    private final Map custodyTypetoCustodyIdMap = new HashMap();

    private String _offenderId;

    private double _bailIndex;

    private String _currentCustody;

    private String _obId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWelcomeBannerLabel();
        showPoliceReports();
        createTextFieldList();
        setCrimeTypeChoiceBoxItems();
        setCustodyTypeChoiceBoxItems();
        updateCustodyButton.setDisable(true);
        setDatePickerFormat();
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
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        boolean confirm = ConfirmBox.displayConfirmBox(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Are you sure you want to change the suspect custody status?");
        if (!confirm) return;
        updateCustody(_offenderId);
    }

    @FXML
    private void onSearchCase(ActionEvent event) {
        if(isObIdTextFieldEmpty()){
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Make Sure All Fields are Filled.");
            return;
        }
        getSuspectCustodyRecords(obNumberTextField.getText());
    }

    @FXML
    private void onReportCrime(ActionEvent event) {
        if (areReportingTextFieldsEmpty() || isChoiceBoxValueEmpty() || isReportingTextAreaEmpty() || isDatePickerValueEmpty()) {
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
                statement.setString(3, offenderNameTextField.getText());
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
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.SUCCESS), "Crime Reported Successfully");
                resetReportingTabInputs();

            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
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

    private Boolean isDatePickerValueEmpty(){
        return dateDatePicker.getValue() == null;
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
        _obId = obNumber;
        Connection connection = MysqlConnector.connectDB();

        if(connection != null){
            try {
                PreparedStatement st = (PreparedStatement) connection.prepareStatement("SELECT cases.Offender_Id, citizens.Name, `custody types`.`Custody_Type`, `crime types`.`bail_index` FROM cases INNER JOIN citizens ON cases.Offender_Id = citizens.`National ID` INNER JOIN offenders ON cases.Offender_Id = offenders.National_Id INNER JOIN `custody types` ON offenders.Custody_Id = `custody types`.`Type_Id` INNER JOIN `crime types` ON cases.Crime_Type = `crime types`.`Type_Id` WHERE cases.OB_id = ?");
                st.setString(1, obNumber);
                ResultSet res = st.executeQuery();

                if (res.next()) {

                    _currentCustody = res.getString("Custody_Type");
                    _bailIndex = Double.parseDouble(res.getString("bail_index"));
                    suspectNameLabel.setText(res.getString("Name"));
                    currentCustodyLabel.setText(_currentCustody);
                    bailFeeLabel.setText(String.valueOf(_bailIndex *Data.BASE_BAIL));
                    _offenderId = res.getString("Offender_Id");
                }
                //To enable the user to be able to click in the update button
                updateCustodyButton.setDisable(false);
            } catch (Exception ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void updateCustody(String offenderId){

        String newCustody = custodyTypeChoiceBox.getValue();
        System.out.println(newCustody + " " + _currentCustody);
        if(Objects.equals(newCustody, _currentCustody)){
            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "New custody status Should be different from current custody status");
            return;
        }

        if(Objects.equals(newCustody, "Released")){
            boolean isPaymentMade = false;

            try {
                isPaymentMade = getBailPayment();
            } catch (Exception e) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), e.getMessage());
            }

            if(!isPaymentMade){
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.WARNING), "Update unsuccessful as the payment has not been performed.");
                return;
            }
            logBails();
        }

        Connection connection = MysqlConnector.connectDB();
        if(connection != null){
            try {

                PreparedStatement st = (PreparedStatement)connection.prepareStatement ("UPDATE `offenders` SET `Custody_Id` = ? WHERE `offenders`.`National_Id` = ?");
                st.setString(1, custodyTypetoCustodyIdMap.get(custodyTypeChoiceBox.getValue()).toString());
                st.setString(2, offenderId);
                int res = st.executeUpdate();

                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.SUCCESS), "Custody Updated Successfully.");
            } catch (SQLException ex) {
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
            }
        }else{
            System.out.println("The connection is not available");
        }
    }

    private void sendReporterEmail(String obNumber){

        HashMap<emailInfo, String> emailInfoMap = new HashMap ();
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
                    emailInfoMap.put(emailInfo.RECIPIENT, recipient);
                    emailInfoMap.put(emailInfo.EMAIL_SUBJECT, Data.getReporterEmailSubject());
                    emailInfoMap.put(emailInfo.EMAIL_BODY, Data.getReporterEmailBody(recipientName, reportDate, reportTime, obNumber));
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
        HashMap<emailInfo, String> emailInfoMap = new HashMap ();
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
                    emailInfoMap.put(emailInfo.RECIPIENT, recipient);
                    emailInfoMap.put(emailInfo.EMAIL_SUBJECT, Data.getInvestigatorEmailSubject());
                    emailInfoMap.put(emailInfo.EMAIL_BODY, Data.getInvestigatorEmailBody(investigatorName, obNumber));
                    emailInfoMap.put(Data.emailInfo.EMAIL_TYPE, Data.EMAIL_TYPES.NEW_CASE_NOTIFICATION.toString());

                    SendEmail.notification(emailInfoMap);
                }

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

    private boolean getBailPayment() throws Exception{
        String url = "https://run.mocky.io/v3/215ff84e-623c-404c-ae1c-246c18585e9f";
        if(Math.random() > 0.5){
            url = "https://run.mocky.io/v3/1aa9b088-8787-4f0e-aa33-417b89febeff";
        }
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        if(responseCode != 200){
            throw new Exception("Error: " + responseCode);
        }

        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print in String
        System.out.println(response.toString());

        //Read JSON response
        JSONObject myResponse = new JSONObject(response.toString());
        return myResponse.getInt("transactionStatus") == 200;
    }

    private void logBails(){

        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        String today = dateFormatter.format(LocalDate.now());

        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("INSERT INTO `bails` (`Transaction_Id`, `Amount`, `OB_Id`, `Date`) VALUES (NULL, ?, ?, ?) ");
                statement.setString(1, String.valueOf((_bailIndex * Data.BASE_BAIL)));
                statement.setString(2, _obId);
                statement.setString(3, today);

                int res = statement.executeUpdate();

            } catch (SQLException ex) {
                PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
                Logger.getLogger(PoliceDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("The connection is not available");
        }
    }
}
