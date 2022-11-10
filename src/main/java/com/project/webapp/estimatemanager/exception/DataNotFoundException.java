package com.project.webapp.estimatemanager.exception;

public class DataNotFoundException extends Exception {
    private String message;

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String message) {
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
