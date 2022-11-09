package com.project.webapp.estimatemanager.dtos;

import java.util.HashSet;
import java.util.Set;

public class EstimateDto {
    private Long id;
    private Float price;
    private String clientEmail;
    private String employeeEmail;
    private String productName;
    private Set<String> optionsNames = new HashSet<>();
}
