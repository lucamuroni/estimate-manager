package com.project.webapp.estimatemanager.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    private Long id;
    private String name;
    public RoleDto(String name) {
        this.name = name;
    }
}
