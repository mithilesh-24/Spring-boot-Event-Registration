package com.mithilesh.eventmanagement.exception;

import com.mithilesh.eventmanagement.dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgeRestrictionException.class)
    public ResponseEntity<?> handleAgeRestriction(AgeRestrictionException ex) {
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<?> handeEventNotFound(EventNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(EventDateException.class)
    public ResponseEntity<?> handleEventDate(EventDateException ex){
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(InvalidUserPasswordException.class)
    public ResponseEntity<?> handleInvalidUserPassword(InvalidUserPasswordException ex){
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null
                )
        );
    }


    @ExceptionHandler(EventAlreadyExist.class)
    public ResponseEntity<?> handleEventAlreadyExist(EventAlreadyExist ex){
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleNotValidation(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(),error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        404,
                        "validation errors",
                        errors
                )
        );
    }

    @ExceptionHandler()
    public ResponseEntity<?> handleUserAlreadyExisted(MethodArgumentNotValidException ex){

        return ResponseEntity.badRequest().body("bad request");
    }
}
