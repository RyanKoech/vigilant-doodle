package com.example.vigilantdoodle.utilities;

import com.example.vigilantdoodle.ApplicationLogin;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendEmail {

    //Send email functions
    public static void notification(String recepient) throws MessagingException {

        System.err.println("Prepareing to send message...");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        /*
            NOTE YOU NEED TO TURN NO 'Allow less secure apps in the security settings of the email address'
        */
        String email = "address@email.random";
        String password = "password";

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password); //To change body of generated methods, choose Tools | Templates.
            }

        });

        Message message = prepareMessage(session, email, recepient);
        javax.mail.Transport.send(message);
        System.out.println("Message Sent Successfully");
    }

    //Prepare email to be sent
    private static Message prepareMessage(Session session, String email, String recepient){

        try {
            String[] recipientList = recepient.split(",");
            InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, recipientAddress);
            message.setSubject("Upcoming Court Meeting For Case ID: " + Data.POLICE_ID);
            message.setText("Dear Litigant,\n\nWe hope you have been doing well. This is to remind you that you are expected to report to court within the next 24hours. Kindly avail yourself early enough to avoid inconviences \n\n"
                    + "Make Sure you carry all that you are required to a have and incase you see that you may not attend communicae in due time which is 48hrs before the meeting else face the penalty.\n\n"
                    + "Thank you and stay safe making sure you abid by the law at all times.\n\n"
                    + "Kind Regards,\n"
                    + "The Judiciary");
            return message;

        } catch (Exception ex) {
            Logger.getLogger(ApplicationLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
