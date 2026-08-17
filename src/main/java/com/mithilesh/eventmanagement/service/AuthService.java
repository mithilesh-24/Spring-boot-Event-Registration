package com.mithilesh.eventmanagement.service;

import com.mithilesh.eventmanagement.dto.Request.LoginRequest;
import com.mithilesh.eventmanagement.dto.Request.SignupRequest;
import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.AgeRestrictionException;
import com.mithilesh.eventmanagement.exception.UserAlreadyExistsException;
import com.mithilesh.eventmanagement.repository.UserRepo;
import com.mithilesh.eventmanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final BlacklistedTokensService blacklistedTokensService;
    /**
     * To save the user in DB
     *
     * @param dto contains the body to save in repository
     * @throws UserAlreadyExistsException if the user already existed
     * @throws AgeRestrictionException if the age is less than 18
     */
    public void register(SignupRequest dto){

        if(userRepo.findByEmail(dto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("The user is already existed");
        }

        if (Period.between(dto.getDob(), LocalDate.now()).getYears() < 18)
            throw new AgeRestrictionException("User must be Atleast 18 years old");

        Users users = new Users();
        users.setFirstName(dto.getFirstName());
        users.setLastName(dto.getLastName());
        users.setDob(dto.getDob());
        users.setGender(dto.getGender());
        users.setPassword(passwordEncoder.encode(dto.getPassword()));
        users.setEmail(dto.getEmail());

        userRepo.save(users);
    }

    /**
     * Validate the user for login
     *
     * @param dto contains email and password
     * @return Return Jwt token
     * @throws com.mithilesh.eventmanagement.exception.InvalidUserPasswordException if is the email or password were not correct
     */
    public String login(LoginRequest dto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));

        return jwtService.generateKey(dto.getEmail());
    }

    /**
     * handover the token blacklist to the Blacklist Service
     *
     * @param authorization contain the authorization header
     */
    public void logout(String authorization) {
        String token = authorization.substring(7);
        blacklistedTokensService.addBlackList(token);
    }
}
