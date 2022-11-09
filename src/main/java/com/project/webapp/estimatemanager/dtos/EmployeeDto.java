package com.project.webapp.estimatemanager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private String password;
}
