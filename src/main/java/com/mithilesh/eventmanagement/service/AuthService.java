package com.mithilesh.eventmanagement.service;

import com.mithilesh.eventmanagement.dto.LoginRequest;
import com.mithilesh.eventmanagement.dto.SignupRequest;
import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.AgeRestrictionException;
import com.mithilesh.eventmanagement.repository.UserRepo;
import com.mithilesh.eventmanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(SignupRequest dto){

        if (Period.between(dto.getDob(), LocalDate.now()).getYears() < 18)
            throw new AgeRestrictionException("User must be atleast 18 years old");

        Users users = new Users();
        users.setFirstName(dto.getFirstName());
        users.setLastName(dto.getLastName());
        users.setDob(dto.getDob());
        users.setGender(dto.getGender());
        users.setPassword(passwordEncoder.encode(dto.getPassword()));
        users.setEmail(dto.getEmail());

        userRepo.save(users);
    }

    public String login(LoginRequest dto){
        Authentication authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword());

        return jwtService.generateKey(dto.getEmail());
    }
}
