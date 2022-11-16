package com.project.webapp.estimatemanager.exception;

public class GenericException extends Exception {
    private String message;

    public GenericException() {
        super();
    }

    public GenericException(String message) {
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
