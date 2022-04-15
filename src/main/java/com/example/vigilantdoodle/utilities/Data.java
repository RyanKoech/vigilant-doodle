package com.example.vigilantdoodle.utilities;

import java.util.HashMap;

public class Data {
    public static String POLICE_ID = "";
    public static double BASE_BAIL = 5000.0;
    public static enum emailInfo {
        RECIPIENT, RECIPIENTNAME, OBID, REPORTDATE, REPORTTIME,
    }

    public static  String getEmailBody(final HashMap<emailInfo, String> emailInfoMap){
        return "Dear " + emailInfoMap.get(emailInfo.RECIPIENTNAME) + ",\n\n" +
                "This is to notify the case you reported on " + emailInfoMap.get(emailInfo.REPORTDATE) + " " + emailInfoMap.get(emailInfo.REPORTTIME) + " hase been recorded and our officers" +
                "will get into the matter as soon as possible.\n\n" +
                "Use the OB Number: " + emailInfoMap.get(emailInfo.OBID) + " from now on to report to the case.\n\n" +
                "Yours protective,\n" +
                "The Kenya Police Service";
    }
}
