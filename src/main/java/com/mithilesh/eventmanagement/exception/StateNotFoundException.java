package com.mithilesh.eventmanagement.exception;

public class StateNotFoundException extends RuntimeException {
    public StateNotFoundException(long id) {
        super("Event with this "+ id + "not found");
    }
}
