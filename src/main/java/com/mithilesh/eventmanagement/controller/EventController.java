package com.mithilesh.eventmanagement.controller;


import com.mithilesh.eventmanagement.dto.ApiResponse;
import com.mithilesh.eventmanagement.dto.EventRegisterRequest;
import com.mithilesh.eventmanagement.security.UserPrincipal;
import com.mithilesh.eventmanagement.service.EventService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRegisterRequest event){
        service.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        201,
                        "Created Event",
                        null
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> getEvents(){
        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "All event",
                        service.getEvents()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id){

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "Success",
                        service.getEventById(id)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id,@RequestBody EventRegisterRequest request){

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        204,
                        "Updated the event",
                        service.updateEvent(id,request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id){
        service.deleteEventById(id);
        return ResponseEntity.ok(
          new ApiResponse<>(
                  204,
                  "Event Deleted",
                  null
          )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEvent(@RequestParam String eventName){

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "Success",
                        service.getEventsBySearch(eventName)
                )
        );
    }

    @PostMapping("/register/{id}")
    public ResponseEntity<?> registerEvent(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal){

        service.registerEvent(id,userPrincipal.getUsername());
        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Event Register",
                        null
                )
        );
    }

}
