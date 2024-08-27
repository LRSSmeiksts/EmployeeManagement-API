package com.lrrsmeiksts.EmployeeManagement_API.web.controller;

import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult){
        log.info("Attempting to save a new employee by passing {}", employee);
        if(bindingResult.hasErrors()){
            log.error("New employee has not been saved. Field errors: {}",bindingResult);
            List<String> errorMessages = bindingResult.getFieldErrors().stream().map(
                    error ->error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        if(employee.getId() != null){
            log.error("New employee has not been saved. ID must not be included in the request");
            return ResponseEntity.badRequest().body("ID must not be included in the request");
        }

        Employee savedEmployee = employeeService.saveEmployee(employee);
        log.debug("New Employee is saved: {}", savedEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportEmployees(@RequestParam String department,
                                             @RequestParam int year,
                                             @RequestParam String format) throws IOException {

        log.info("Attempting to find employees by parameters- department: {}, year: {}",department, year);
        List<Employee> employeeList = employeeService.getEmployeesByDepartmentAndDate(department,year);
        log.debug("Size of list of employees found by criteria: {}",employeeList.size());

        byte[] data;
        String contentType;
        String fileName;

            if("csv".equalsIgnoreCase(format) || format == null || format.isEmpty()){
                data = employeeService.exportEmployeesToCSV(employeeList);
                contentType = "text/csv";
                fileName = "employees.csv";
            }
            else if("excel".equalsIgnoreCase(format)){
                data = employeeService.exportEmployeesToExcel(employeeList);
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "employees.xlsx";
            }else{
                return ResponseEntity.badRequest().body("Unsupported format");
            }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"" )
                .body(data);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteEmployeeById(@NotNull @PathVariable Long id){
        log.info("Attempting to delete employee by ID: {}", id);

        if (id <= 0){
            log.warn("Request to delete employee by invalid ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee ID must be a positive number, but provided was: " +id);
        }

        Optional<Employee> employeeToBeDeleted = employeeService.getEmployeeById(id);
        if(employeeToBeDeleted.isEmpty()){
            log.warn("Employee with ID {} is not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employee was found with id "+ id);
        }

        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
