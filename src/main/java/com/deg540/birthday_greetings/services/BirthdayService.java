package com.deg540.birthday_greetings.services;

import com.deg540.birthday_greetings.domain.Employee;
import com.deg540.birthday_greetings.domain.EmployeeRepository;
import com.deg540.birthday_greetings.domain.Mail;
import com.deg540.birthday_greetings.domain.OurDate;
import com.deg540.birthday_greetings.infrastructure.FileSystemEmployeeRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class BirthdayService {
    public static final String SMTP_URL = "localhost";
    public static final String SMTP_PORT = "1025";

    private final EmployeeRepository employeeRepository;

    public BirthdayService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void sendGreetings(String fileName, OurDate ourDate) throws IOException, ParseException,
            AddressException, MessagingException {

        List<Employee> birthdayEmployees = employeeRepository.getEmployees(fileName)
                .stream()
                .filter(employee -> employee.isBirthday(ourDate))
                .collect(Collectors.toList());

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

    private void sendMessage(Mail mail) throws AddressException, MessagingException {
        // Create a mail session
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", SMTP_URL);
        props.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getDefaultInstance(props, null);

        // Construct the message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("sender@here.com"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getRecipient()));
        msg.setSubject(mail.getSubject());
        msg.setText(mail.getBody());

        // Send the message
        sendMessage(msg);
    }

    // made protected for testing :-(
    protected void sendMessage(Message msg) throws MessagingException {
        Transport.send(msg);
    }
}
