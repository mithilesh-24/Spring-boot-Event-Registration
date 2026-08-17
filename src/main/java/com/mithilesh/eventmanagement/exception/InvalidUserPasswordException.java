package com.mithilesh.eventmanagement.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class InvalidUserPasswordException extends UsernameNotFoundException {
    public InvalidUserPasswordException(String message) {
        super(message);
    }
}
