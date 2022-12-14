
package com.project.webapp.estimatemanager.dtos;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstimateDto {
    private Long id;
    private Float price;
    private UserDto client;
    private UserDto employee;
    private ProductDto product;
    private Set<OptDto> options = new HashSet<>();
}