package com.mithilesh.eventmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgeRestrictionException.class)
    public ResponseEntity<String> handleAgeRestriction(AgeRestrictionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handeEventNotFound(EventNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EventDateException.class)
    public ResponseEntity<String> handleEventDate(EventDateException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(InvalidUserPasswordException.class)
    public ResponseEntity<String> handleInvalidUserPassword(InvalidUserPasswordException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(EventAlreadyExist.class)
    public ResponseEntity<String> handleEventAlreadyExist(EventAlreadyExist ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
