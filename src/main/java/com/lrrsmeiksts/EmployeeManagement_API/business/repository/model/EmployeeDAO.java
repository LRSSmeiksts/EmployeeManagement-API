package com.lrrsmeiksts.EmployeeManagement_API.business.repository.model;

import jakarta.persistence.*;
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
