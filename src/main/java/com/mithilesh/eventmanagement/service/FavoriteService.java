package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.EventResponse;
import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.entity.Favorites;
import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.EventNotFoundException;
import com.mithilesh.eventmanagement.exception.UserNotFoundException;
import com.mithilesh.eventmanagement.mapper.EventMapper;
import com.mithilesh.eventmanagement.repository.EventRepo;
import com.mithilesh.eventmanagement.repository.FavoriteRepo;
import com.mithilesh.eventmanagement.repository.UserRepo;
import com.mithilesh.eventmanagement.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final UserRepo userRepo;
    private final FavoriteRepo favoriteRepo;
    private final EventRepo eventRepo;

    public List<EventResponse> getFavorites(String email){
        return favoriteRepo.findAllByUser_Email(email)
                .stream()
                .map(favorites -> EventMapper.toResponse(favorites.getEvent()))
                .sorted((a,b) ->a.getEventName().compareTo(b.getEventName()))
                .toList();
    }

    public void addFavorites(String email,long id) {
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Invalid email"));

        Events events = eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Invalid event ID"));

        Favorites favorites = new Favorites();
        favorites.setEvent(events);
        favorites.setUser(users);

        favoriteRepo.save(favorites);
    }

    public void deleteFavorites(String email, long id) {

        Favorites favorites = favoriteRepo
                .findByUser_EmailAndEvent_EventId(email,id)
                .orElseThrow(() -> new EventNotFoundException("Invalid event id to delete"));

        favoriteRepo.delete(favorites);
    }
}
