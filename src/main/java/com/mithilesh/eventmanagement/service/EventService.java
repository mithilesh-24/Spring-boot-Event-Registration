package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.EventRegisterRequest;
import com.mithilesh.eventmanagement.dto.EventResponse;
import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.entity.Registration;
import com.mithilesh.eventmanagement.entity.States;
import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.EventDateException;
import com.mithilesh.eventmanagement.exception.EventNotFoundException;
import com.mithilesh.eventmanagement.exception.NotAuthenticationException;
import com.mithilesh.eventmanagement.exception.UserNotFoundException;
import com.mithilesh.eventmanagement.mapper.EventMapper;
import com.mithilesh.eventmanagement.repository.EventRepo;
import com.mithilesh.eventmanagement.repository.RegistrationRepo;
import com.mithilesh.eventmanagement.repository.StateRepo;
import com.mithilesh.eventmanagement.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    final private EventRepo eventRepo;
    final private StateRepo stateRepo;
    final private RegistrationRepo registrationRepo;
    final private UserRepo userRepo;

    public void createEvent(EventRegisterRequest request){
       Events events = new Events();
        LocalDate today = LocalDate.now();

        if(request.getEventDate().isBefore(today.plusDays(10)))
            throw new EventNotFoundException("Event date must be at least 15 days from today");

        if(request.getRegistrationEndDate().isBefore(today.plusDays(5)))
            throw new EventDateException("Registration end date must be at least 5 from today");

        States state = stateRepo.findByStateName(request.getStateName()).orElseGet(()-> {
            States newstates = new States();
            newstates.setStateName(request.getStateName());
            return stateRepo.save(newstates);
        });

        events.setEventName(request.getEventName());
        events.setDescription(request.getDiscription());
        events.setPopularityScores(0);
        events.setRegistrationEndDate(request.getRegistrationEndDate());
        events.setEventDate(request.getEventDate());
        events.setVenue(request.getVenue());
        events.setState(state);

        eventRepo.save(events);

    }

    public List<EventResponse> getEvents() {

        return eventRepo.findAll()
                .stream()
                .map(events -> EventMapper.toResponse(events))
                .sorted((a, b) -> a.getEventName().compareTo(b.getEventName()))
                .toList();
    }

    @Transactional
    public EventResponse getEventById(long id) {
        Events event =  eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Event not Found"));
        event.setPopularityScores(event.getPopularityScores()+1);
        
        return EventMapper.toResponse(event);
    }

    public void updateEvent(long id,EventRegisterRequest request){
        Events events = eventRepo.findById(id).orElseThrow(
                () -> new EventNotFoundException("To update the event, the give id is invalid")
        );

        States states = stateRepo.findByStateName(request.getStateName()).orElseGet(()-> {
            States newstates = new States();
            newstates.setStateName(request.getStateName());
            return stateRepo.save(newstates);
        });

        events.setEventId(id);
        events.setState(states);
        events.setEventDate(request.getEventDate());
        events.setEventName(request.getEventName());
        events.setRegistrationEndDate(request.getRegistrationEndDate());
        events.setDescription(request.getDiscription());
        events.setVenue(request.getVenue());

        eventRepo.save(events);
    }

    public void deleteEventById(long id){
        eventRepo.findById(id).orElseThrow(
                ()->new EventNotFoundException("Invalid event to delete")
        );
        eventRepo.deleteById(id);
    }

    public List<?> getEventsBySearch(String eventName) {

        return eventRepo.findByEventNameContainingIgnoreCase(eventName)
                .stream()
                .map(events -> EventMapper.toResponse(events))
                .toList();
    }

    public void registerEvent(long id,String email) {

        if(email == null){
          throw new UserNotFoundException("Email not found");
        }

        Users user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Invalid user to register"));

        Events events = eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));

        Registration registration = new Registration();
        registration.setEvent(events);
        registration.setUser(user);

        registrationRepo.save(registration);
    }
}
