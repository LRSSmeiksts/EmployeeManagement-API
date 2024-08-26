package com.lrrsmeiksts.EmployeeManagement_API.business.service;

import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;

import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    void deleteEmployeeById(Long id);

    Optional<Employee> getEmployeeById(Long id);
}
