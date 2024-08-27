package com.lrrsmeiksts.EmployeeManagement_API.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.EmployeeRepository;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class EmployeeControllerIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EmployeeService employeeService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        employeeRepository.deleteAll();
    }

    @Test
    void saveEmployee_ShouldReturnCreatedStatus() throws Exception {
        Employee employee = new Employee(null, "Example", "IT", LocalDate.of(2023, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Example"));
    }

    @Test
    void deleteEmployeeById_ShouldReturnNoContent() throws Exception {
        EmployeeDAO employeeDAO = new EmployeeDAO(null, "Example", "IT", LocalDate.of(2023, 1, 1));
        EmployeeDAO savedEmployeeDAO = employeeRepository.save(employeeDAO);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", savedEmployeeDAO.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployeeById_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employee was found with id 999"));
    }
}