package com.project.webapp.estimatemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> clientNotFoundHandler(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_FOUND.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameAlreadyTakenException.class)
    public ResponseEntity<ErrorResponse> nameAlreadyTakenException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> productNotFoundException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_FOUND.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> optionNotFoundException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_FOUND.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EstimateNotFoundException.class)
    public ResponseEntity<ErrorResponse> estimateNotFoundException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_FOUND.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> dataNotFoundException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_FOUND.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessNotAllowedException.class)
    public ResponseEntity<ErrorResponse> accessNotAllowedException(Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.FORBIDDEN.value());
        error.setMessaggio(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
