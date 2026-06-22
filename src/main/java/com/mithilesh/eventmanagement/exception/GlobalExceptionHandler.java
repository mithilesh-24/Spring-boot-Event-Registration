package com.mithilesh.eventmanagement.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgeRestrictionException.class)
    public ResponseEntity<String> handleAgeRestriction(AgeRestrictionException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
