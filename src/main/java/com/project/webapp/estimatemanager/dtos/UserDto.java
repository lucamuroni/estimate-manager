package com.project.webapp.estimatemanager.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<RoleDto> roles;
}
