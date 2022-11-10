package com.project.webapp.estimatemanager.exception;

public class OptionNotFoundException extends Exception{
    private String message;

    public OptionNotFoundException() {
        super();
    }

    public OptionNotFoundException(String message) {
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
