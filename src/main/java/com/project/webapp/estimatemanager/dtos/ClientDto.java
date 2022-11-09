package com.project.webapp.estimatemanager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String password;
}
