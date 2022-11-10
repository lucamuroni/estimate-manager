package com.project.webapp.estimatemanager.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int codice;
    private String messaggio;
}
