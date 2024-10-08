package com.lrrsmeiksts.EmployeeManagement_API.business.repository;

import com.lrrsmeiksts.EmployeeManagement_API.business.repository.model.EmployeeDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDAO, Long> {
    List<EmployeeDAO> findByDepartmentAndDateAfter(String department, LocalDate date);
}
