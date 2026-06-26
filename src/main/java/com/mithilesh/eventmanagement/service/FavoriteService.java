package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.EventResponse;
import com.mithilesh.eventmanagement.mapper.EventMapper;
import com.mithilesh.eventmanagement.repository.FavoriteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepo favoriteRepo;

    public List<EventResponse> getFavorites(String email){
        return favoriteRepo.findAllByUser_Email(email)
                .stream()
                .map(favorites -> EventMapper.toResponse(favorites.getEvent()))
                .sorted((a,b) ->a.getEventName().compareTo(b.getEventName()))
                .toList();
    }
}
