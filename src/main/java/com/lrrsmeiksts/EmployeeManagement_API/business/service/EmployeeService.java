package com.lrrsmeiksts.EmployeeManagement_API.business.service;

import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    void deleteEmployeeById(Long id);

    Optional<Employee> getEmployeeById(Long id);

    List<Employee> getEmployeesByDepartmentAndDate(String department, int year);

    byte[] exportEmployeesToCSV(List<Employee> employeeList) throws IOException;

    byte[] exportEmployeesToExcel(List<Employee> employeeList) throws  IOException;

}
