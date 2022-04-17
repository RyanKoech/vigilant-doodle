package com.example.vigilantdoodle.utilities;

import com.example.vigilantdoodle.ApplicationLogin;
import com.example.vigilantdoodle.privates.PrivateData;
import com.example.vigilantdoodle.utilities.Data.emailInfo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
        System.out.println("Message Sent Successfully");
    }

    //Prepare email to be sent
    private static Message prepareMessage(Session session, String sender,final HashMap<emailInfo, String> emailInfoMap){

        try {
            InternetAddress recipientAddress = new InternetAddress(emailInfoMap.get(emailInfo.RECIPIENT));
            Message message = new MimeMessage(session);
            message.addHeader("Content-Type", "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, recipientAddress);
            message.setSubject(Data.getReporterEmailSubject());
            message.setText(Data.getReporterEmailBody(emailInfoMap));
            message.setSentDate(new Date());
            return message;

        } catch (Exception ex) {
            Logger.getLogger(ApplicationLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
