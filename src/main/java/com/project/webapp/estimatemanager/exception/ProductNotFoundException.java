package com.project.webapp.estimatemanager.exception;

public class ProductNotFoundException extends Exception {
    private String message;

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message) {
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
