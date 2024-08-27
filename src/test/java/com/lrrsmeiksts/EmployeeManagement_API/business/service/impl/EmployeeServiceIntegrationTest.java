package com.lrrsmeiksts.EmployeeManagement_API.business.service.impl;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.EmployeeRepository;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void saveEmployee_ShouldSaveEmployee() {
        Employee employee = new Employee(null, "Example", "IT", LocalDate.of(2023, 1, 1));
        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee.getId());
        assertEquals("Example", savedEmployee.getName());
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() {
        EmployeeDAO employeeDAO = new EmployeeDAO(null, "Example", "IT", LocalDate.of(2023, 1, 1));
        EmployeeDAO savedEmployeeDAO = employeeRepository.save(employeeDAO);

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(savedEmployeeDAO.getId());
        assertTrue(foundEmployee.isPresent());
        assertEquals("Example", foundEmployee.get().getName());
    }

    @Test
    void deleteEmployeeById_ShouldDeleteEmployee() {
        EmployeeDAO employeeDAO = new EmployeeDAO(null, "Example", "IT", LocalDate.of(2023, 1, 1));
        EmployeeDAO savedEmployeeDAO = employeeRepository.save(employeeDAO);

        employeeService.deleteEmployeeById(savedEmployeeDAO.getId());

        Optional<EmployeeDAO> foundEmployeeDAO = employeeRepository.findById(savedEmployeeDAO.getId());
        assertFalse(foundEmployeeDAO.isPresent());
    }

    @Test
    void getEmployeesByDepartmentAndDate_ShouldReturnEmployees() {
        EmployeeDAO employeeDAO1 = new EmployeeDAO(null, "Example", "IT", LocalDate.of(2023, 12, 1));
        EmployeeDAO employeeDAO2 = new EmployeeDAO(null, "Example", "IT", LocalDate.of(2023, 2, 1));
        employeeRepository.save(employeeDAO1);
        employeeRepository.save(employeeDAO2);

        List<Employee> employees = employeeService.getEmployeesByDepartmentAndDate("IT", 2023);
        assertEquals(2, employees.size());
    }
}