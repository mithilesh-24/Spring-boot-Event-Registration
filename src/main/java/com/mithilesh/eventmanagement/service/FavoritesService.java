package com.mithilesh.eventmanagement.service;


import com.mithilesh.eventmanagement.dto.Response.EventResponse;
import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.entity.Favorites;
import com.mithilesh.eventmanagement.entity.Users;
import com.mithilesh.eventmanagement.exception.EventAlreadyExist;
import com.mithilesh.eventmanagement.exception.EventNotFoundException;
import com.mithilesh.eventmanagement.exception.UserNotFoundException;
import com.mithilesh.eventmanagement.mapper.EventMapper;
import com.mithilesh.eventmanagement.repository.EventRepo;
import com.mithilesh.eventmanagement.repository.FavoriteRepo;
import com.mithilesh.eventmanagement.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesService {
    private final UserRepo userRepo;
    private final FavoriteRepo favoriteRepo;
    private final EventRepo eventRepo;

    /**
     * to get list of favorite event
     *
     * @param email user email
     * @return list of favorites event
     */
    public List<EventResponse> getFavorites(String email){
        return favoriteRepo.findAllByUser_Email(email)
                .stream()
                .map(favorites -> EventMapper.toResponse(favorites.getEvent()))
                .sorted((a,b) ->a.getEventName().compareTo(b.getEventName()))
                .toList();
    }

    /**
     * To add event to user
     *
     * @param email user email
     * @param id event to add
     */
    public void addFavorites(String email,long id) {
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Invalid email"));

        Events events = eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Invalid event ID"));

        if(favoriteRepo.findByUser_EmailAndEvent_EventId(email,events.getEventId()).isPresent()){
            throw new EventAlreadyExist("This event is already in favorites");
        }
        Favorites favorites = new Favorites();
        favorites.setEvent(events);
        favorites.setUser(users);

        favoriteRepo.save(favorites);
    }

    /**
     * to delete a favorite event
     *
     * @param email user email
     * @param id event to delete
     */
    public void deleteFavorites(String email, long id) {

        Favorites favorites = favoriteRepo
                .findByUser_EmailAndEvent_EventId(email,id)
                .orElseThrow(() -> new EventNotFoundException("Invalid event id to delete"));

        favoriteRepo.delete(favorites);
    }
}
