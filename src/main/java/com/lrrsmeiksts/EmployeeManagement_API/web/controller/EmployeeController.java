package com.lrrsmeiksts.EmployeeManagement_API.web.controller;

import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findEmployeeById(@NonNull @PathVariable Long id){
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
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
