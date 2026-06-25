package com.mithilesh.eventmanagement.security;

import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.InvalidUserPasswordException;
import com.mithilesh.eventmanagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Users users = userRepo.findByEmail(email).orElseThrow(
                () -> new InvalidUserPasswordException("Invalid Email Or Password"));

        return new UserPrincipal(users);
    }
}
