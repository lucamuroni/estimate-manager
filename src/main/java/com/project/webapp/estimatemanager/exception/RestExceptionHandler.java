package com.project.webapp.estimatemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NameAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> nameAlreadyTakenException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> genericException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.BAD_REQUEST.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> noSuchElementException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NO_CONTENT.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }
}
