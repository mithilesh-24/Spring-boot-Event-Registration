package com.mithilesh.eventmanagement.repository;


import com.mithilesh.eventmanagement.controller.FavoriteController;
import com.mithilesh.eventmanagement.dto.EventResponse;
import com.mithilesh.eventmanagement.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorites,Long> {
    List<Favorites> findAllByUser_Email(String email);
}
