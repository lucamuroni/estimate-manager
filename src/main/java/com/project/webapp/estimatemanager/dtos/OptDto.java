package com.project.webapp.estimatemanager.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptDto {
    private Long id;
    private String type;
    private String name;
}
