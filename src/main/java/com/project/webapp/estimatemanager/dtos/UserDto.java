package com.project.webapp.estimatemanager.dtos;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<RoleDto> roles;
}
