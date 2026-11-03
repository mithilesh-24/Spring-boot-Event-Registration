package com.mithilesh.eventmanagement.controller;


import com.mithilesh.eventmanagement.dto.Request.SearchRequest;
import com.mithilesh.eventmanagement.dto.Response.ApiResponse;
import com.mithilesh.eventmanagement.dto.Request.EventRegisterRequest;
import com.mithilesh.eventmanagement.dto.Response.EventResponse;
import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.security.UserPrincipal;
import com.mithilesh.eventmanagement.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService service;

    /**
     * To create the event
     *
     * @param event dto which validated and content data to create event
     * @return event details with success message
     */
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRegisterRequest event){

        log.info("Event Creation request received");

        service.createEvent(event);

        log.info("Event creation request Completed");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Created Event"
                )
        );
    }

    /**
     * To see list of event
     * <p>If user is register it will return event in sorted order (Statename -> favorite -> event name -> popularity)
     * Or events will be returned as Statename -> event name
     * </p>
     *
     * @param authentication object to check if the user is register or not
     * @return List of events
     */
    @GetMapping
    public ResponseEntity<?> getEvents(Authentication authentication){
        List<?> result = null;

        if(authentication == null || !authentication.isAuthenticated()){
            log.debug("Fetching all public events");

            result = service.getEvents();
        }
        else{
            log.debug("Fetching events for authenticated user");

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            result = service.getEvents(userPrincipal.getUsername());
        }

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "All event",
                        result
                )
        );
    }

    /**
     * To view the specific event
     *
     * @param id event id
     * @param authentication to check the user is login or not
     * @return the event contail event details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id,Authentication authentication) {

        EventResponse result = null;

        if(authentication == null || !authentication.isAuthenticated()){

            log.debug("Fetching event with id: {} for unauthenticated user", id);

            result = service.getEventById(id);
        }
        else{
            log.debug("Fetching event with id: {} for authenticated user", id);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            result = service.getEventById(id,userPrincipal.getUsername());
        }


        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "Event Found",
                        result
                )
        );
    }

    /**
     * To update the event
     *
     * @param id event id
     * @param request body data to update the event
     * @return updated event with message
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id,@RequestBody EventRegisterRequest request){

        log.info("Event update request received with id: {}",id);

        EventResponse eventResponse = service.updateEvent(id,request);

        log.info("Event update request with id: {} completed successfully",id);

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        204,
                        "Event is Updated",
                        eventResponse
                )
        );
    }

    /**
     * To delete the event
     *
     * @param id event id to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id){

        log.info("Event Deleted request with id: {} received",id);
        service.deleteEventById(id);

        log.info("Event delete request for id: {} completed Successfully",id);
        return ResponseEntity.noContent().build();
    }

    /**
     * To search the event
     *
     * @param  search object contain details to search
     * @return List of event similar to event name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchEvent(@ModelAttribute SearchRequest search){

        log.debug("Searching events with provided filters");

        List<?> eventResponses = service.getEventsBySearch(search);

        log.debug("Searching events with provided filters completed");
        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "Search result",
                        eventResponses
                )
        );
    }

    /**
     * To register the event
     *
     * @param id event
     * @param userPrincipal contains email and password
     * @return success message
     */
    @PostMapping("/{id}/registrations")
    public ResponseEntity<?> registerEvent(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal){

        log.info("Event registration request with id: {} received", id);
        service.registerEvent(id,userPrincipal.getUsername());

        log.info("Event registration request for id: {} completed successfully",id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        201,
                        "Event registered successfully"
                )
        );
    }

}
