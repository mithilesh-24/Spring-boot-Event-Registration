package com.mithilesh.eventmanagement.controller;

import com.mithilesh.eventmanagement.dto.Response.ApiResponse;
import com.mithilesh.eventmanagement.dto.Response.EventResponse;
import com.mithilesh.eventmanagement.security.UserPrincipal;
import com.mithilesh.eventmanagement.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
@Slf4j
public class FavoriteController {

    private final FavoritesService favoritesService;
    /**
     * to get the list of favorites events
     * @param userPrincipal contains the user data
     * @return list of favorites
     */
    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal){
        log.debug("Fetching favorites events request");
        List<EventResponse> eventResponses = favoritesService.getFavorites(userPrincipal.getUsername());
        log.debug("Fetching favorites events request completed");
        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "returning favorites event",
                        eventResponses
                )
        );
    }

    /**
     * add favorite event
     *
     * @param userPrincipal contains user data
     * @param id eventId
     * @return success message
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> addFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id){
        log.info("Add Favorite event request with id: {}",id);
        favoritesService.addFavorites(userPrincipal.getUsername(),id);
        log.info("Add Favorite event request for id: {} completed successfully",id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        201,
                        "Successfully added"
                )
        );
    }

    /**
     * to remove favorites event
     *
     * @param userPrincipal contains user data
     * @param id eventId
     * @return successful message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long id){
        log.info("Delete favorite event request with id: {}",id);
        favoritesService.deleteFavorites(userPrincipal.getUsername(),id);
        log.info("Delete favorite event request for id: {} completed successfully",id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        204,
                        "Deleted Successfully"
                )
        );
    }

}
