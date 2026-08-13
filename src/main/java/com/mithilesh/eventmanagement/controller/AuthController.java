package com.mithilesh.eventmanagement.controller;

import com.mithilesh.eventmanagement.dto.Response.ApiResponse;
import com.mithilesh.eventmanagement.dto.Request.LoginRequest;
import com.mithilesh.eventmanagement.dto.Request.SignupRequest;
import com.mithilesh.eventmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final private AuthService authService;

    /**
     * Register a new user
     * The request body is validated
     *
     * @param user contains the user details
     * @return response message indicating the success
     * @throws  com.mithilesh.eventmanagement.exception.UserAlreadyExistsException if the email already existed
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest user){

        authService.register(user);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        201,
                        "Register SuccessFully",
                        null
                )
        );
    }

    /**
     * Login using email and password
     * The request body is validated
     *
     * @param user contains email and password for login
     * @return Response body containing success message and Jwt Token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest user){
        return ResponseEntity.ok(
                new ApiResponse<>(
                    200,
                    "Login SuccessFully",
                        authService.login(user)
                )
        );
    }
}