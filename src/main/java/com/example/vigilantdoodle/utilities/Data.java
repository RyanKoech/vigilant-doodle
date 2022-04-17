package com.example.vigilantdoodle.utilities;

import java.util.HashMap;

public class Data {
    public static String POLICE_ID = "";
    public static double BASE_BAIL = 5000.0;
    public static enum emailInfo {
        RECIPIENT, RECIPIENTNAME, OBID, REPORTDATE, REPORTTIME, EMAIL_BODY, EMAIL_SUBJECT, INVESTIGATING_OFFICER
    }

    public static  String getReporterEmailBody(final HashMap<emailInfo, String> emailInfoMap){
        return "Dear " + emailInfoMap.get(emailInfo.RECIPIENTNAME) + ",\n\n" +
                "This is to notify you that the case you reported on the date " + emailInfoMap.get(emailInfo.REPORTDATE) + " at " + emailInfoMap.get(emailInfo.REPORTTIME) + "HRS hase been recorded and our investigating officers " +
                "will dive into the matter and begin investigations as soon as possible.\n\n" +
                "Use the OB Number: " + emailInfoMap.get(emailInfo.OBID) + " from now on to reference the case.\n\n" +
                "Yours protective,\n" +
                "The Kenya Police Service";
    }

    public static String getReporterEmailSubject(){
        return "Reported Case Confirmation";
    }

    public static String getInvestigatorEmailBody(final HashMap<emailInfo, String> emailInfoMap){
        return "Dear Inspector" + emailInfoMap.get(emailInfo.INVESTIGATING_OFFICER) + ",\n\n" +
                "This is to notify you have been assigned as the Lead Investigating Officer over the case #" + emailInfoMap.get(emailInfo.OBID) + ". " +
                "If you are not at the station, kindly report within the next 24 hours to gather the required documents and information to begin carrying out " +
                "investigations into the case.\n\n" +
                "Yours protective,\n" +
                "The Kenya Police Service";
    }

    public static String getInvestigatorEmailSubject(){
        return "New Case Investigation Appointment";
    }
}
