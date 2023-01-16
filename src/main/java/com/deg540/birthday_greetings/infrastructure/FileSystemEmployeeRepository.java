package com.deg540.birthday_greetings.infrastructure;

import com.deg540.birthday_greetings.domain.Employee;
import com.deg540.birthday_greetings.domain.EmployeeRepository;
import com.deg540.birthday_greetings.domain.OurDate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemEmployeeRepository implements EmployeeRepository {
    public List<Employee> getByBirthday(OurDate ourDate) {
        List<Employee> employees = new ArrayList<>();
        try {
            BufferedReader in = null;
            in = new BufferedReader(new FileReader("src/test/resources/employee_data.txt"));
            String str = in.readLine(); // skip header
            while ((str = in.readLine()) != null) {
                String[] employeeData = str.split(", ");
                Employee employee = new Employee(employeeData[1], employeeData[0], employeeData[2], employeeData[3]);
                employees.add(employee);
            }
        } catch (ParseException | IOException ignored) {
        }

        return employees
                .stream()
                .filter(employee -> employee.isBirthday(ourDate))
                .collect(Collectors.toList());
    }
}
