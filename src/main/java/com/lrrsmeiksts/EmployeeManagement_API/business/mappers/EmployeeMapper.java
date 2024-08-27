package com.lrrsmeiksts.EmployeeManagement_API.business.mappers;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDAO employeeToEmployeeDAO(Employee employee){
        if(employee == null){
            return null;
        }
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.setId(employee.getId());
        employeeDAO.setName(employee.getName());
        employeeDAO.setDepartment(employee.getDepartment());
        employeeDAO.setDate(employee.getDate());

        return employeeDAO;
    }

    public Employee employeeDAOToEmployee(EmployeeDAO employeeDAO){
        if(employeeDAO == null){
            return null;
        }
        Employee employee = new Employee();
        employee.setId(employeeDAO.getId());
        employee.setName(employeeDAO.getName());
        employee.setDepartment(employeeDAO.getDepartment());
        employee.setDate(employeeDAO.getDate());

        return employee;
    }
}
