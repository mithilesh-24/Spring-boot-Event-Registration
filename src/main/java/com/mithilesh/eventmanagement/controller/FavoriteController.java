package com.mithilesh.eventmanagement.controller;

import com.mithilesh.eventmanagement.dto.ApiResponse;
import com.mithilesh.eventmanagement.security.UserPrincipal;
import com.mithilesh.eventmanagement.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal){

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        200,
                        "returning favorites event",
                        favoriteService.getFavorites(userPrincipal.getUsername())
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> addFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id){

        favoriteService.addFavorites(userPrincipal.getUsername(),id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        201,
                        "Successfully added",
                        null
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long id){
        favoriteService.deleteFavorites(userPrincipal.getUsername(),id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        204,
                        "Deleted Successfully",
                        null
                )
        );
    }

}
