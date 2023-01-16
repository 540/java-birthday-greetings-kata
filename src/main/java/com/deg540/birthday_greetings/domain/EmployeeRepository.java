package com.deg540.birthday_greetings.domain;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> getByBirthday(OurDate ourDate);
}
