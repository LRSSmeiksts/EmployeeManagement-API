package com.lrrsmeiksts.EmployeeManagement_API.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {

    @NotNull
    @Min(value = 1, message = "ID must be bigger than 0")
    @Max(value = Long.MAX_VALUE, message = " ID must be smaller than 9223372036854775807")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String department;

    @NotNull
    @DateTimeFormat(pattern = " yyyy-MM-dd")
    private LocalDate date;

}
