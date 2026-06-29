package com.mithilesh.eventmanagement.dto;


import com.mithilesh.eventmanagement.entity.States;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EventRegisterRequest {

    @NotBlank(message = "Event name is required")
    private String eventName;

    @NotBlank(message = "State Name is required")
    private String stateName;

    @NotNull(message = "Event Date is required")
    private LocalDate eventDate;

    @NotNull(message = "Registration end date is required")
    private LocalDate registrationEndDate;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Venue is required")
    private String venue;
}