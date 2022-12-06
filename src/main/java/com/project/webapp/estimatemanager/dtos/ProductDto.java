package com.project.webapp.estimatemanager.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String imageUrl;
}
