package com.project.webapp.estimatemanager.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginDto {
    private String email;
    private String password;
}
