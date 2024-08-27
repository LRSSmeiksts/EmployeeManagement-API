package com.lrrsmeiksts.EmployeeManagement_API.business.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.lrrsmeiksts.EmployeeManagement_API.business.handlers.GeneralDatabaseException;
import com.lrrsmeiksts.EmployeeManagement_API.business.mappers.EmployeeMapper;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.EmployeeRepository;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveEmployee_ShouldReturnSavedEmployee() {
        Employee employee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));
        EmployeeDAO employeeDAO = new EmployeeDAO(1L, "Example", "IT", LocalDate.of(2023, 1, 1));
        EmployeeDAO savedEmployeeDAO = new EmployeeDAO(1L, "Example", "IT", LocalDate.of(2023, 1, 1));

        when(employeeMapper.employeeToEmployeeDAO(employee)).thenReturn(employeeDAO);
        when(employeeRepository.save(employeeDAO)).thenReturn(savedEmployeeDAO);
        when(employeeMapper.employeeDAOToEmployee(savedEmployeeDAO)).thenReturn(employee);

        Employee result = employeeService.saveEmployee(employee);
        assertNotNull(result);
        assertEquals(employee, result);
    }

    @Test
    void deleteEmployeeById_ShouldSucceed() {
        Long id = 1L;
        doNothing().when(employeeRepository).deleteById(id);

        assertDoesNotThrow(() -> employeeService.deleteEmployeeById(id));
    }

    @Test
    void deleteEmployeeById_ShouldThrowException_WhenDataAccessExceptionOccurs() {
        Long id = 1L;
        doThrow(new DataAccessException("Database error") {}).when(employeeRepository).deleteById(id);

        assertThrows(GeneralDatabaseException.class, () -> employeeService.deleteEmployeeById(id));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() {
        Long id = 1L;
        EmployeeDAO employeeDAO = new EmployeeDAO(id, "Example", "IT", LocalDate.of(2023, 1, 1));
        Employee employee = new Employee(id, "Example", "IT", LocalDate.of(2023, 1, 1));

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employeeDAO));
        when(employeeMapper.employeeDAOToEmployee(employeeDAO)).thenReturn(employee);

        Optional<Employee> result = employeeService.getEmployeeById(id);
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

    @Test
    void getEmployeesByDepartmentAndDate_ShouldReturnListOfEmployees() {
        String department = "IT";
        int year = 2023;
        EmployeeDAO employeeDAO = new EmployeeDAO(1L, "Example", department, LocalDate.of(year, 1, 1));
        Employee employee = new Employee(1L, "Example", department, LocalDate.of(year, 1, 1));
        List<EmployeeDAO> employeeDAOList = List.of(employeeDAO);
        List<Employee> employeeList = List.of(employee);

        when(employeeRepository.findByDepartmentAndDateAfter(department, LocalDate.of(year, 1, 1)))
                .thenReturn(employeeDAOList);
        when(employeeMapper.employeeDAOToEmployee(employeeDAO)).thenReturn(employee);

        List<Employee> result = employeeService.getEmployeesByDepartmentAndDate(department, year);
        assertEquals(employeeList, result);
    }

    @Test
    void exportEmployeesToCSV_ShouldReturnByteArray() throws IOException {
        Employee employee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));
        List<Employee> employeeList = List.of(employee);

        byte[] result = employeeService.exportEmployeesToCSV(employeeList);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void exportEmployeesToExcel_ShouldReturnByteArray() throws IOException {
        Employee employee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));
        List<Employee> employeeList = List.of(employee);

        byte[] result = employeeService.exportEmployeesToExcel(employeeList);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}