package com.mithilesh.eventmanagement.exception;

import com.mithilesh.eventmanagement.dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * when the age is invalid it is thrown
     * Invalid age should be greater than 18
     *
     * @param ex AgeRestrictionException
     * @return ResponsEntity object
     */
    @ExceptionHandler(AgeRestrictionException.class)
    public ResponseEntity<?> handleAgeRestriction(AgeRestrictionException ex) {
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                )
        );
    }

    /**
     * When Event is Not found it is thrown
     *
     * @param ex Exception object
     * @return Not Found Status as response
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<?> handeEventNotFound(EventNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                )
        );
    }

    /**
     * When the date is invalid
     *
     * @param ex Exception object
     * @return Bad Request as response object
     */
    @ExceptionHandler(EventDateException.class)
    public ResponseEntity<?> handleEventDate(EventDateException ex){
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                )
        );
    }

    /**
     * When the username or password is incorrect the exception is thrown
     *
     * @param ex which is the InvalidUserNameException
     * @return Response object
     */
    @ExceptionHandler(InvalidUserPasswordException.class)
    public ResponseEntity<?> handleInvalidUserPassword(InvalidUserPasswordException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(
                new ApiResponse<>(
                        HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage()
                )
        );
    }


    /**
     * The event is already existed
     *
     * @param ex Exception object
     * @return Conflict code as response
     */
    @ExceptionHandler(EventAlreadyExist.class)
    public ResponseEntity<?> handleEventAlreadyExist(EventAlreadyExist ex){
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(
                new ApiResponse<>(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        null
                )
        );
    }

    /**
     * Validation Exception
     *
     * @param ex Exception
     * @return BadRequest as Abject
     */
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

    /**
     * Exception handler for user already existed
     *
     * @param ex which is the exception class
     * @return ResponseEntity object
     */
    @ExceptionHandler()
    public ResponseEntity<?> handleUserAlreadyExisted(UserAlreadyExistsException ex){

        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(
                new ApiResponse<>(
                        HttpStatus.CONFLICT.value(),
                        "User Already existed"
                )
        );
    }

    /**
     * When the password is invalid it is thrown
     *
     * @param ex Exception
     * @return unauthorized as response
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED).body(
                        new ApiResponse<>(
                                HttpStatus.UNAUTHORIZED.value(),
                                "Invalid email or password"
                        )
                );
    }
}
