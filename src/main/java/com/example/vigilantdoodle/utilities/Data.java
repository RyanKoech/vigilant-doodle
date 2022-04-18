package com.example.vigilantdoodle.utilities;

import java.util.HashMap;

public class Data {
    public static String POLICE_ID = "";
    public static double BASE_BAIL = 5000.0;
    public static enum emailInfo {
        RECIPIENT, RECIPIENTNAME, OBID, REPORTDATE, REPORTTIME, EMAIL_BODY, EMAIL_SUBJECT, INVESTIGATING_OFFICER
    }
    public static enum FEEDBACK_MESSAGES{
        ERROR, WARNING, SUCCESS
    }

    public static String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public static HashMap<FEEDBACK_MESSAGES, String> FEEDBACK_STRINGS = new HashMap<>() {{
        put(FEEDBACK_MESSAGES.ERROR, "ERROR");
        put(FEEDBACK_MESSAGES.WARNING, "WARNING");
        put(FEEDBACK_MESSAGES.SUCCESS, "SUCCESS");
    }};

    public static  String getReporterEmailBody(String recipientName, String reportDate, String reportTime, String obId){
        return "Dear " + recipientName + ",\n\n" +
                "This is to notify you that the case you reported on the date " + reportDate+ " at " + reportTime + "HRS hase been recorded and our investigating officers " +
                "will dive into the matter and begin investigations as soon as possible.\n\n" +
                "Use the OB Number: " + obId + " from now on to reference the case.\n\n" +
                "Yours protective,\n" +
                "The Kenya Police Service";
    }

    public static String getReporterEmailSubject(){
        return "Reported Case Confirmation";
    }

    public static String getInvestigatorEmailBody(String investigatorName, String obId){
        return "Dear Inspector" + investigatorName + ",\n\n" +
                "This is to notify you have been assigned as the Lead Investigating Officer over the case #" + obId + ". " +
                "If you are not at the station, kindly report within the next 24 hours to gather the required documents and information to begin carrying out " +
                "investigations into the case.\n\n" +
                "Yours protective,\n" +
                "The Kenya Police Service";
    }

    public static String getInvestigatorEmailSubject(){
        return "New Case Investigation Appointment";
    }
}
