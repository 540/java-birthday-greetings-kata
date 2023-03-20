package com.deg540.birthday_greetings.infrastructure;

import com.deg540.birthday_greetings.domain.Mail;
import com.deg540.birthday_greetings.domain.Mailer;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class TransportMailer implements Mailer {
    public static final String SMTP_URL = "localhost";
    public static final String SMTP_PORT = "1025";

    public void sendMail(Mail mail) {
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", SMTP_URL);
        props.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("sender@here.com"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getRecipient()));
            msg.setSubject(mail.getSubject());
            msg.setText(mail.getBody());

            Transport.send(msg);
        } catch (MessagingException ignored) {
        }
    }
}
