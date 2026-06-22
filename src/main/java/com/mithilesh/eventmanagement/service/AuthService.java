package com.mithilesh.eventmanagement.service;

import com.mithilesh.eventmanagement.dto.LoginRequest;
import com.mithilesh.eventmanagement.dto.SignupRequest;
import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.AgeRestrictionException;
import com.mithilesh.eventmanagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;


@Service
@RequiredArgsConstructor
public class AuthService {
    final UserRepo repo;

    public void register(SignupRequest dto){

        if (Period.between(dto.getDob(), LocalDate.now()).getYears() < 18)
            throw new AgeRestrictionException("User must be 18 years old");

        Users users = new Users();
        users.setFirst_name(dto.getFirst_name());
        users.setLast_name(dto.getLast_name());
        users.setDob(dto.getDob());
        users.setGender(dto.getGender());
        users.setPassword(dto.getPassword());

        repo.save(users);
    }

    public void login(LoginRequest dto){

    }
}
