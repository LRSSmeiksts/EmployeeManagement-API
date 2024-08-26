package com.lrrsmeiksts.EmployeeManagement_API.business.mappers;

import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "date", source = "date")
    EmployeeDAO employeeToEmployeeDAO(Employee employee);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "date", source = "date")
    Employee employeeDAOToEmployee(EmployeeDAO employeeDAO);
}
