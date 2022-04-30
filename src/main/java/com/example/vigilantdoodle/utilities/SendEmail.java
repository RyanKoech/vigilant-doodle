package com.example.vigilantdoodle.utilities;

import com.example.vigilantdoodle.ApplicationLogin;
import com.example.vigilantdoodle.PoliceDashboardController;
import com.example.vigilantdoodle.privates.PrivateData;
import com.example.vigilantdoodle.ui_utilities.PopUpAlert;
import com.example.vigilantdoodle.utilities.Data.emailInfo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendEmail {

    //Send email functions
    public static void notification(final HashMap<emailInfo, String> emailInfoMap) throws MessagingException {

        System.err.println("Prepareing to send message...");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        /*
            NOTE YOU NEED TO TURN NO 'Allow less secure apps in the security settings of the email address'
            Store the information in com.example.vigilantdoodle.privates (Directory Ignored by Git)
        */
        String email = PrivateData.EMAIL_ADDRESS;
        String password = PrivateData.EMAIL_PASSWORD;

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password); //To change body of generated methods, choose Tools | Templates.
            }

        });

        Message message = prepareMessage(session, email, emailInfoMap);
        javax.mail.Transport.send(message);
        logEmailSending(emailInfoMap.get(emailInfo.RECIPIENT), emailInfoMap.get(emailInfo.EMAIL_TYPE));
    }

    //Prepare email to be sent
    private static Message prepareMessage(Session session, String sender,final HashMap<emailInfo, String> emailInfoMap){

        try {
            InternetAddress recipientAddress = new InternetAddress(emailInfoMap.get(emailInfo.RECIPIENT));
            Message message = new MimeMessage(session);
            message.addHeader("Content-Type", "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, recipientAddress);
            message.setSubject(emailInfoMap.get(emailInfo.EMAIL_SUBJECT));
            message.setText(emailInfoMap.get(emailInfo.EMAIL_BODY));
            message.setSentDate(new Date());
            return message;

        } catch (Exception ex) {
            Logger.getLogger(ApplicationLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void logEmailSending(String  recipientEmail, String emailType){
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        String dateToday = dateFormatter.format(LocalDate.now());

        Connection connection = MysqlConnector.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statement = (PreparedStatement) connection.prepareStatement("INSERT INTO `email logs` (`Id`, `Email_Type`, `Recipient_Address`, `Date`) VALUES (NULL, ?, ?, ?) ");
                statement.setString(1, emailType);
                statement.setString(2, recipientEmail);
                statement.setString(3, dateToday);

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
