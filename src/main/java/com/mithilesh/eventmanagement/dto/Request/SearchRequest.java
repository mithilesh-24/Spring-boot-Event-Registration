package com.mithilesh.eventmanagement.dto.Request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
public class SearchRequest {
    private String eventName;
    private String stateName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationEndDate;
    private String description;
    private String venue;
    private String query;
    private String hideCovid;
}
