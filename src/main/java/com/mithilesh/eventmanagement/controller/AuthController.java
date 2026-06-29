package com.mithilesh.eventmanagement.controller;

import com.mithilesh.eventmanagement.dto.ApiResponse;
import com.mithilesh.eventmanagement.dto.LoginRequest;
import com.mithilesh.eventmanagement.dto.SignupRequest;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest user){

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