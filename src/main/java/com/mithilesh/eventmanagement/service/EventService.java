package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.EventRegisterRequest;
import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.entity.States;
import com.mithilesh.eventmanagement.exception.EventDateException;
import com.mithilesh.eventmanagement.exception.EventNotFoundException;
import com.mithilesh.eventmanagement.exception.StateNotFoundException;
import com.mithilesh.eventmanagement.repository.EventRepo;
import com.mithilesh.eventmanagement.repository.StateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    final private EventRepo eventRepo;
    final private StateRepo stateRepo;

    public void registerEvent(EventRegisterRequest request){
       Events events = new Events();
        LocalDate today = LocalDate.now();

        if(request.getEventDate().isAfter(today.plusDays(10)))
            throw new EventNotFoundException("Event date must be at least 15 days from today");

        if(request.getRegistrationEndDate().isAfter(today.plusDays(5)))
            throw new EventDateException("Registration end date must be at least 5 from today");

        States state = stateRepo.findById(request.getStateId()).orElseThrow(() -> new StateNotFoundException(request.getStateId()));

        events.setEventName(request.getEventName());
        events.setDescription(request.getDiscription());
        events.setPopularityScores(0);
        events.setRegistrationEndDate(request.getRegistrationEndDate());
        events.setEventDate(request.getEventDate());
        events.setVenue(request.getVenue());
        events.setState(state);

        eventRepo.save(events);

    }

    public List<?> getEvents() {
        return eventRepo.findAll();
    }

    public Events getEventById(long id) {
        return eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Event not Found"));
    }
}
