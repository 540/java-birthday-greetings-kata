package com.deg540.birthday_greetings;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BirthdayService {

    public static final String SMTP_URL = "localhost";
    public static final String SMTP_PORT = "1025";

    public void sendGreetings(String fileName, OurDate ourDate) throws IOException, ParseException,
            AddressException, MessagingException {

        List<Employee> birthdayEmployees = getEmployees(fileName)
                .stream()
                .filter(employee -> employee.isBirthday(ourDate))
                .toList();

        for (Employee employee : birthdayEmployees) {
            Mail mail = createMail(employee);
            sendMessage(mail);
        }
    }

    private static Mail createMail(Employee employee) {
        String recipient = employee.getEmail();
        String body = "Happy Birthday, dear %NAME%!".replace("%NAME%", employee.getFirstName());
        String subject = "Happy Birthday!";

        return new Mail(recipient, body, subject);
    }

    private static List<Employee> getEmployees(String fileName) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine(); // skip header

        List<Employee> employees = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            String[] employeeData = str.split(", ");
            Employee employee = new Employee(employeeData[1], employeeData[0], employeeData[2], employeeData[3]);
            employees.add(employee);
        }

        return employees;
    }

    private void sendMessage(Mail mail) throws AddressException, MessagingException {
        // Create a mail session
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", SMTP_URL);
        props.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getDefaultInstance(props, null);

        // Construct the message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("sender@here.com"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.recipient));
        msg.setSubject(mail.subject);
        msg.setText(mail.body);

        // Send the message
        sendMessage(msg);
    }

    // made protected for testing :-(
    protected void sendMessage(Message msg) throws MessagingException {
        Transport.send(msg);
    }
}
