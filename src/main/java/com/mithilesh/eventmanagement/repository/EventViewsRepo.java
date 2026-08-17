package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.EventViews;
import com.mithilesh.eventmanagement.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventViewsRepo extends JpaRepository<EventViews,Long> {

    Optional<EventViews> findByUsers_EmailAndEvent_EventId(String  email, long id);

    void deleteByEvent(Events events);
}
