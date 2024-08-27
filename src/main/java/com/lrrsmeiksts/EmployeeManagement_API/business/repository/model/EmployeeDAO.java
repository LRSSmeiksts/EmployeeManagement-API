package com.lrrsmeiksts.EmployeeManagement_API.business.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
@Data
public class EmployeeDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="employee_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
