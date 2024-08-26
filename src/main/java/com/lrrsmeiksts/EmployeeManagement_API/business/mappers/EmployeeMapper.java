package com.lrrsmeiksts.EmployeeManagement_API.business.mappers;

import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDAO employeeToEmployeeDAO(Employee employee);

    Employee employeeDAOToEmployee(EmployeeDAO employeeDAO);
}
