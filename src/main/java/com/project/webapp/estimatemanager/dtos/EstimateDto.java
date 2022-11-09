package com.project.webapp.estimatemanager.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EstimateDto {
    private Long id;
    private Float price;
    private String clientEmail;
    private String employeeEmail;
    private String productName;
    private Set<OptDto> options = new HashSet<>();
}
