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
     *Register a new user
     *
     * @param user contains the user details
     * @return respons message
     * @throws
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