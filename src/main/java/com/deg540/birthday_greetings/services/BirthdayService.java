package com.deg540.birthday_greetings.services;

import com.deg540.birthday_greetings.domain.*;

import java.util.List;

public class BirthdayService {
    private final EmployeeRepository employeeRepository;
    private final Mailer mailer;

    public BirthdayService(EmployeeRepository employeeRepository, Mailer mailer) {
        this.employeeRepository = employeeRepository;
        this.mailer = mailer;
    }

    public void sendGreetings(OurDate ourDate) {
        List<Employee> birthdayEmployees = employeeRepository.getByBirthday(ourDate);

        for (Employee employee : birthdayEmployees) {
            Mail mail = createMail(employee);
            mailer.sendMail(mail);
        }
    }

    private static Mail createMail(Employee employee) {
        String recipient = employee.getEmail();
        String body = "Happy Birthday, dear %NAME%!".replace("%NAME%", employee.getFirstName());
        String subject = "Happy Birthday!";

        return new Mail(recipient, body, subject);
    }
}
