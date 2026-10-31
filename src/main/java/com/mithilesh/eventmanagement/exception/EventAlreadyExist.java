package com.mithilesh.eventmanagement.exception;

public class EventAlreadyExist extends RuntimeException {
    public EventAlreadyExist(String message) {
        super(message);
    }
}
