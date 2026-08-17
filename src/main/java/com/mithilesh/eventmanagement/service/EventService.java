package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.Request.EventRegisterRequest;
import com.mithilesh.eventmanagement.dto.Request.SearchRequest;
import com.mithilesh.eventmanagement.dto.Response.EventResponse;
import com.mithilesh.eventmanagement.entity.*;
import com.mithilesh.eventmanagement.exception.EventAlreadyExist;
import com.mithilesh.eventmanagement.exception.EventDateException;
import com.mithilesh.eventmanagement.exception.EventNotFoundException;
import com.mithilesh.eventmanagement.exception.UserNotFoundException;
import com.mithilesh.eventmanagement.mapper.EventMapper;
import com.mithilesh.eventmanagement.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    final private EventRepo eventRepo;
    final private StateRepo stateRepo;
    final private RegistrationRepo registrationRepo;
    final private UserRepo userRepo;
    final private EventViewsRepo eventViewsRepo;
    final private FavoriteRepo favoriteRepo;
    final private CovidAffectedAreaRepo covidAffectedAreaRepo;

    /**
     * To create event
     *
     * @param request contains the date to create the event
     * @throws EventDateException for date validation
     * @throws EventAlreadyExist if the event already existed
     */
    @Transactional
    public void createEvent(EventRegisterRequest request){
       Events events = new Events();
       LocalDate today = LocalDate.now();

        States state = stateRepo.findByStateNameIgnoreCase(request.getStateName()).orElseGet(()-> {
            States newstates = new States();
            newstates.setStateName(request.getStateName());
            return stateRepo.save(newstates);
        });

        eventRepo.findByEventName(request.getEventName(),state.getStateId()).ifPresent(
                e -> {
                    throw new EventAlreadyExist("Event Already Exist");
                });

        if(request.getEventDate().isBefore(today.plusDays(10)))
            throw new EventDateException("Event date must be at least 15 days from today");

        if(request.getRegistrationEndDate().isBefore(today.plusDays(5)))
            throw new EventDateException("Registration end date must be at least 5 from today");


        events.setEventName(request.getEventName());
        events.setDescription(request.getDescription());
        events.setPopularityScores(0);
        events.setRegistrationEndDate(request.getRegistrationEndDate());
        events.setEventDate(request.getEventDate());
        events.setVenue(request.getVenue());
        events.setState(state);

        eventRepo.save(events);

    }

    /**
     * To get the list of events
     *
     * if the user is not authenticated this is called
     *
     * @return list of event as Response object
     */
    public List<EventResponse> getEvents() {
        return eventRepo.findAllByOrderByEventNameAsc()
                .stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    /**
     * to get the list of events
     *
     * if the user is authentication the list of events in sorted order
     *
     * @param email to get the user favorite events
     * @return list of event
     * @throws UserNotFoundException if the user not exist
     */
    public List<?> getEvents(String email){
        userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Username not found"));


        return eventRepo.findSortedEvent(email)
                .stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    /**
     * To view specific event
     * The user is athenticated and the view will be incremented if the user visit the event for first time
     *
     * @param id event id
     * @param email user email
     * @return event resposne contain event details
     * @throws EventNotFoundException if the event is not found
     * @throws UserNotFoundException if the user email is not found
     */
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

    /**
     * To get the specific event
     * If the user doesn't log in the event view will not be incremented
     *
     * @param id event id
     * @return Event object
     * @throws EventNotFoundException if the event is not found
     */
    public EventResponse getEventById(long id){
        Events event =  eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Event not Found"));
        return EventMapper.toResponse(event);
    }

    /**
     * To update event
     *
     * @param id event it
     * @param request contain details to update the event
     * @return the updated event
     * @throws EventNotFoundException if the event is not found
     */
    public EventResponse updateEvent(long id,EventRegisterRequest request){
        Events events = eventRepo.findById(id).orElseThrow(
                () -> new EventNotFoundException("To update the event, the give id is invalid")
        );

        States states = stateRepo.findByStateNameIgnoreCase(request.getStateName()).orElseGet(()-> {
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

    /**
     * To delete the event
     *
     * @param id event id
     * @throws EventNotFoundException if the event is not found
     */
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


    /**
     * To search and filter the event
     *
     *
     * @param searchRequest contain details to search and filter
     * @return list of events
     */
    public List<?> getEventsBySearch(SearchRequest searchRequest) {

        Specification<Events> spec = Specification.unrestricted();

        if(searchRequest.getEventName() != null){
            spec = spec.and(
                    (root, query, cb) ->
                        cb.like(
                                cb.lower(root.get("eventName")),
                                "%" +searchRequest.getEventName().toLowerCase()+ "%"
                        )
            );
        }
        if(searchRequest.getDescription() != null){
            spec = spec.and(
                    (root, query, cb) ->
                            cb.like(root.get("description"),
                            "%" + searchRequest.getDescription() + "%"
                            )
            );
        }
        if(searchRequest.getStateName() != null){
            Optional<States> state = stateRepo.findByStateNameIgnoreCase(searchRequest.getStateName());

            if(state.isPresent()) {
                spec = spec.and(
                        (root, query, cb) ->
                                cb.equal(root.get("stateId"),
                                        state.get().getStateId()
                                )
                );
            }
        }
        if(searchRequest.getStartDate() != null && searchRequest.getEndDate() != null){
            spec = spec.and(
                    (root, query, cb) ->
                            cb.between(
                                    root.get("eventDate"),
                                    searchRequest.getStartDate(),
                                    searchRequest.getEndDate()
                            )
            );
        }
        else if(searchRequest.getEndDate()!= null){
            spec = spec.and(
                    (root, query, cb) ->
                        cb.lessThanOrEqualTo(
                                root.get("eventDate"),
                                searchRequest.getEndDate()
                        )
            );
        }
        else if(searchRequest.getStartDate() != null){
            spec = spec.and(
                    (root, query, cb) ->
                        cb.greaterThanOrEqualTo(
                                root.get("eventDate"),
                                searchRequest.getStartDate()
                        )
            );
        }
        if(searchRequest.getRegistrationEndDate() != null){
            spec = spec.and(
                    (root, query, cb) ->
                        cb.equal(
                                root.get("registrationEndDate"),
                                searchRequest.getRegistrationEndDate()
                        )
            );
        }
        if(searchRequest.getQuery() != null &&
                !searchRequest.getQuery().isBlank()){
            String searchString = "%" +  searchRequest.getQuery().toLowerCase() + "%";
            spec = spec.and(
                    (root, query, cb) ->
                            cb.or(
                                cb.like(
                                    cb.lower(root.get("eventName")),
                                    searchString
                                ),
                                cb.like(
                                        cb.lower(root.get("description")),
                                        searchString
                                ),
                                cb.like(
                                        cb.lower(root.get("venue")),
                                        searchString
                                )
                            )

            );
        }
        if(searchRequest.isHideCovid()){
            List<Long> stateList = covidAffectedAreaRepo.findAll()
                    .stream()
                    .map(CovidAffectedArea::getStates)
                    .map(States::getStateId)
                    .toList();

            spec = spec.and(
                        (root, query, cb) ->
                            cb.not(
                                    root.get("states")
                                            .get("stateId").in(stateList)
                            )
                    );
        }

        return eventRepo.findAll(spec)
                .stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    /**
     * to register for specific event
     * @param id event id
     * @param email user email
     * @throws EventNotFoundException if the event is not found
     * @throws UserNotFoundException if the user not found
     */
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