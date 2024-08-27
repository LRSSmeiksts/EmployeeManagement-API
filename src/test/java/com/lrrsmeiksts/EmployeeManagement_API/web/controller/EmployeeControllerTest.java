package com.lrrsmeiksts.EmployeeManagement_API.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void saveEmployee_ShouldReturnCreatedStatus() throws Exception {
        Employee employee = new Employee(null, "Example", "IT", LocalDate.of(2023, 1, 1));
        Employee savedEmployee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));

        when(employeeService.saveEmployee(employee)).thenReturn(savedEmployee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Example"));
    }

    @Test
    void saveEmployee_ShouldReturnBadRequest_WhenIdIsPresent() throws Exception {
        Employee employee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ID must not be included in the request"));
    }

    @Test
    void deleteEmployeeById_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        when(employeeService.getEmployeeById(id)).thenReturn(Optional.of(new Employee()));

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployeeById_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        Long id = 1L;
        when(employeeService.getEmployeeById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employee was found with id 1"));
    }

    @Test
    void exportEmployees_ShouldReturnCsv() throws Exception {
        Employee employee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));
        List<Employee> employeeList = List.of(employee);
        byte[] csvData = "ID,Name,Department,Date\n1,Example,IT,2023-01-01\n".getBytes();

        when(employeeService.getEmployeesByDepartmentAndDate("IT", 2023)).thenReturn(employeeList);
        when(employeeService.exportEmployeesToCSV(employeeList)).thenReturn(csvData);

        mockMvc.perform(get("/employees/export")
                        .param("department", "IT")
                        .param("year", "2023")
                        .param("format", "csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().bytes(csvData));
    }

    @Test
    void exportEmployees_ShouldReturnExcel() throws Exception {
        Employee employee = new Employee(1L, "Example", "IT", LocalDate.of(2023, 1, 1));
        List<Employee> employeeList = List.of(employee);
        byte[] excelData = {};

        when(employeeService.getEmployeesByDepartmentAndDate("IT", 2023)).thenReturn(employeeList);
        when(employeeService.exportEmployeesToExcel(employeeList)).thenReturn(excelData);

        mockMvc.perform(get("/employees/export")
                        .param("department", "IT")
                        .param("year", "2023")
                        .param("format", "excel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(content().bytes(excelData));
    }
}