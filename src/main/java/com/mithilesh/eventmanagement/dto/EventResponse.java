package com.mithilesh.eventmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventResponse {

    private String eventName;
    private String StateName;
    private long popularityScores = 0;
    private LocalDate eventDate;
    private LocalDate registrationEndDate;
    private String description;
    private String venue;
}
