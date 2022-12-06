
package com.project.webapp.estimatemanager.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
public class EstimateDto {
    private Long id;
    private Float price;
    private UserDto client;
    private UserDto employee;
    private ProductDto product;
    private Set<OptDto> options = new HashSet<>();
}