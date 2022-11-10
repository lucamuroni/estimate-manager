package com.project.webapp.estimatemanager.exception;

public class AccessNotAllowedException extends Exception {
    private String message;

    public AccessNotAllowedException() {
        super();
    }

    public AccessNotAllowedException(String message) {
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
