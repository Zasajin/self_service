package org.example;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * The calss to send emails.
 */
public class EmailSender {
  private final String emailFrom = "jakihin@gmail.com";
  private final String appPassword = "ofgf smfv vrnl mnkx";

  /**
   * Method to send an email.
   *
   * @param emailTo recieptents email
   * @param subject the subject of the mail
   * @param message the message of the mail
   * @throws Exception if something goes wrong
   */
  public void sendMail(String emailTo, String subject, String message) throws Exception {
    MimeMessage m = new MimeMessage(getEmailSession());
    m.setFrom(new InternetAddress(emailFrom));
    m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
    m.setSubject(subject);
    m.setText(message);
    Transport.send(m);
  }

  private Session getEmailSession() {
    return Session.getInstance(getProperties(), new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(emailFrom, appPassword);
      }
    });
  }

  private Properties getProperties() {
    Properties p = new Properties();
    p.put("mail.smtp.auth", "true");
    p.put("mail.smtp.starttls.enable", "true");
    p.put("mail.smtp.host", "smtp.gmail.com");
    p.put("mail.smtp.port", "587");
    p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    return p;
  }
}
