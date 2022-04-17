package com.example.vigilantdoodle.utilities;

import java.util.HashMap;

public class Data {
    public static String POLICE_ID = "";
    public static double BASE_BAIL = 5000.0;
    public static enum emailInfo {
        RECIPIENT, RECIPIENTNAME, OBID, REPORTDATE, REPORTTIME,
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
}
