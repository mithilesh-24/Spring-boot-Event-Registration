package com.mithilesh.eventmanagement.mapper;

import com.mithilesh.eventmanagement.dto.EventResponse;
import com.mithilesh.eventmanagement.entity.Events;

public class EventMapper {

    public static EventResponse toResponse(Events events){
        return
                new EventResponse(
                        events.getEventId(),
                        events.getEventName(),
                        events.getState().getStateName(),
                        events.getPopularityScores(),
                        events.getEventDate(),
                        events.getRegistrationEndDate(),
                        events.getDescription(),
                        events.getVenue()
                );
    }
}
