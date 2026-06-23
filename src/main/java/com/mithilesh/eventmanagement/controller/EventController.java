package com.mithilesh.eventmanagement.controller;


import com.mithilesh.eventmanagement.dto.EventRegisterRequest;
import com.mithilesh.eventmanagement.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerEvent(@Valid @RequestBody EventRegisterRequest event){
        service.registerEvent(event);
        return ResponseEntity.ok().body("Event Created");
    }

    @GetMapping
    public ResponseEntity<?> getEvents(){
        return ResponseEntity.ok().body(service.getEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id){

        return ResponseEntity.ok().body(service.getEventById(id));
    }

}
