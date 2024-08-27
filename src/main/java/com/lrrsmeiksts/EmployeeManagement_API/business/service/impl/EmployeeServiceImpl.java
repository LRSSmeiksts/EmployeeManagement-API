package com.lrrsmeiksts.EmployeeManagement_API.business.service.impl;

import com.lrrsmeiksts.EmployeeManagement_API.business.handlers.FileProcessingException;
import com.lrrsmeiksts.EmployeeManagement_API.business.handlers.GeneralDatabaseException;
import com.lrrsmeiksts.EmployeeManagement_API.business.mappers.EmployeeMapper;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.EmployeeRepository;
import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import com.lrrsmeiksts.EmployeeManagement_API.business.service.EmployeeService;
import com.lrrsmeiksts.EmployeeManagement_API.model.Employee;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
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
            log.info("Employee has been saved: {}",savedEmployeeDAO);
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

    @Override
    public List<Employee> getEmployeesByDepartmentAndDate(String department, int year){
        List<Employee> employeeList = employeeRepository.findByDepartmentAndDateAfter(department,LocalDate.of(year,1,1) )
                .stream().map(employeeMapper::employeeDAOToEmployee).toList();
        log.info("Employees found by the criteria. List size: {}",employeeList.size());
        return  employeeList;

    }

    @Override
    public byte[] exportEmployeesToCSV(List<Employee> employeeList) throws IOException {
        try{
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer);
            String[] header ={"ID", "Name", "Department", "Date"};
            csvWriter.writeNext(header);

            for(Employee employee : employeeList){
                String[] data={
                        employee.getId().toString(),
                        employee.getName(),
                        employee.getDepartment(),
                        employee.getDate().toString()
                };
                csvWriter.writeNext(data);
            }

            csvWriter.close();
            return writer.toString().getBytes(StandardCharsets.UTF_8);
        }catch (IOException e){
            log.error("Error exporting the data to CSV: {} ",e.getMessage());
            throw new FileProcessingException("Error exporting the data: ", e);
        }


    }

    @Override
    public byte[] exportEmployeesToExcel(List<Employee> employeeList) throws IOException {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Employees");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Department");
            headerRow.createCell(3).setCellValue("Date");

            int rowIdX = 1;
            for(Employee employee: employeeList){
                Row row = sheet.createRow(rowIdX++);
                row.createCell(0).setCellValue(employee.getId());
                row.createCell(1).setCellValue(employee.getName());
                row.createCell(2).setCellValue(employee.getDepartment());
                row.createCell(3).setCellValue(employee.getDate().toString());
            }
            try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }catch (IOException e){
            log.error("Error exporting the data to xlsx: {} ",e.getMessage());
            throw new FileProcessingException("Error exporting the data: ", e);
        }
    }
}
