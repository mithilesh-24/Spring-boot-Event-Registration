package com.mithilesh.eventmanagement.dto;


import jakarta.validation.constraints.NotBlank;

public class EventRegisterRequest {

    @NotBlank(message = "Event name is required")
    String event_name;



}
