package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.EventRegisterRequest;
import com.mithilesh.eventmanagement.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    final private EventRepo repo;

    public void registerEvent(EventRegisterRequest event){

    }
}
