package com.project.webapp.estimatemanager.exception;

public class EstimateNotFoundException extends Exception {
    private String message;

    public EstimateNotFoundException() {
        super();
    }

    public EstimateNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
