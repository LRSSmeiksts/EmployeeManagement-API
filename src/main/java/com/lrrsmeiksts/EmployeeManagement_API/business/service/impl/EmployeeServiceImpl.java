package com.lrrsmeiksts.EmployeeManagement_API.business.service.impl;

import com.lrrsmeiksts.EmployeeManagement_API.business.handlers.GeneralDatabaseException;
import com.lrrsmeiksts.EmployeeManagement_API.business.mappers.EmployeeMapper;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.EmployeeRepository;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper){
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        try{
            EmployeeDAO employeeDAO = employeeMapper.employeeToEmployeeDAO(employee);
            EmployeeDAO savedEmployeeDAO = employeeRepository.save(employeeDAO);
            return employeeMapper.employeeDAOToEmployee(savedEmployeeDAO);
        }catch (DataAccessException e){
            throw new GeneralDatabaseException("Failed to save employee due to a database error ",e);
        }
    }

    @Override
    public void deleteEmployeeById(Long id) {
        try{
            employeeRepository.deleteById(id);
            log.info("Employee with ID: {} has been deleted", id);
        }catch(DataAccessException e){
            throw new GeneralDatabaseException("Failed to delete an employee due to a database error ",e);
        }
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> employeeById = employeeRepository.findById(id)
                .flatMap(employee -> Optional.ofNullable(employeeMapper.employeeDAOToEmployee(employee)));
        log.info("Employee with ID {} is {}", id, employeeById);
        return employeeById;
    }
}
