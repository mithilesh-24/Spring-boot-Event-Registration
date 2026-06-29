package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.EventRegisterRequest;
import com.mithilesh.eventmanagement.dto.EventResponse;
import com.mithilesh.eventmanagement.entity.*;
import com.mithilesh.eventmanagement.exception.EventAlreadyExist;
import com.mithilesh.eventmanagement.exception.EventDateException;
import com.mithilesh.eventmanagement.exception.EventNotFoundException;
import com.mithilesh.eventmanagement.exception.UserNotFoundException;
import com.mithilesh.eventmanagement.mapper.EventMapper;
import com.mithilesh.eventmanagement.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {
    final private EventRepo eventRepo;
    final private StateRepo stateRepo;
    final private RegistrationRepo registrationRepo;
    final private UserRepo userRepo;
    final private EventViewsRepo eventViewsRepo;
    final private FavoriteRepo favoriteRepo;

    @Transactional
    public void createEvent(EventRegisterRequest request){
       Events events = new Events();
       LocalDate today = LocalDate.now();

        eventRepo.findByEventName(request.getEventName()).ifPresent(
                e -> {
                    throw new EventAlreadyExist("Event Already Exist");
                });

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
        events.setDescription(request.getDescription());
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

    public List<?> getEvents(String email){
        return eventRepo.findSortedEvent(email)
                .stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    @Transactional
    public EventResponse getEventById(long id,String email) {
        Events event =  eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Event not Found"));
        Users user = userRepo.findByEmail(email).orElseThrow(() ->new UserNotFoundException("Invalid user"));

        if(eventViewsRepo.findByUsers_EmailAndEvent_EventId(email,id).isEmpty()){
            event.setPopularityScores(event.getPopularityScores() + 1);
            EventViews popularity = new EventViews();
            popularity.setUsers(user);
            popularity.setEvent(event);

            eventViewsRepo.save(popularity);
        }
        
        return EventMapper.toResponse(event);
    }

    @Transactional
    public EventResponse updateEvent(long id,EventRegisterRequest request){
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
        events.setDescription(request.getDescription());
        events.setVenue(request.getVenue());

        eventRepo.save(events);

        return EventMapper.toResponse(events);
    }

    @Transactional
    public void deleteEventById(long id){
        Events events = eventRepo.findById(id).orElseThrow(
                ()->new EventNotFoundException("Invalid event to delete")
        );

        favoriteRepo.deleteByEvent(events);
        registrationRepo.deleteByEvent(events);
        eventViewsRepo.deleteByEvent(events);

        eventRepo.delete(events);
    }

    public List<?> getEventsBySearch(String eventName) {

        return eventRepo.findByEventNameContainingIgnoreCase(eventName)
                .stream()
                .map(events -> EventMapper.toResponse(events))
                .toList();
    }

    @Transactional
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