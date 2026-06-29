package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepo extends JpaRepository<Events,Long> {

    List<Events> findByEventNameContainingIgnoreCase(String name);

    @Query("""
        select e
        from Events e
        where replace(lower(e.eventName),' ','') =
            replace (lower(:eventName),' ','' )
    """)
    Optional<Events> findByEventName( String eventName);


    @Query("""
        SELECT e
        FROM Events e
        LEFT JOIN Favorites f
        ON f.event = e AND f.user.email = :email
        ORDER BY
            e.state.stateName,
            CASE WHEN f.favoritesId = NULL THEN 0 ELSE 1 END DESC,
            e.popularityScores DESC
""")
    List<Events> findSortedEvent(String email);
}
