package com.project.webapp.estimatemanager.exception;

public class NameAlreadyTakenException extends Exception {
    private String message;

    public NameAlreadyTakenException() {
        super();
    }

    public NameAlreadyTakenException(String message) {
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
