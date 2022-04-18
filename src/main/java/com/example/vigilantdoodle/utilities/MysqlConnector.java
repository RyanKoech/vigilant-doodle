package com.example.vigilantdoodle.utilities;

import com.example.vigilantdoodle.ui_utilities.PopUpAlert;

import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConnector {
    static final String DB_URL = "jdbc:mysql://localhost:3306/crimes reporting system?allowMultiQueries=true";
    static final String USER = "root";
    static final String PASS = "";
    public static Connection connectDB(){
        Connection conn = null;
        try{

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        }catch(Exception ex){

            PopUpAlert.displayPopUpAlert(Data.FEEDBACK_STRINGS.get(Data.FEEDBACK_MESSAGES.ERROR), ex.getMessage());
            return null;
        }
    }
}
