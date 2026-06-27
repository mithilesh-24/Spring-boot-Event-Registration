package com.mithilesh.eventmanagement.repository;


import com.mithilesh.eventmanagement.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorites,Long> {
    List<Favorites> findAllByUser_Email(String email);

    Optional<Favorites> findByUser_EmailAndEvent_EventId(String email, long id);
}
