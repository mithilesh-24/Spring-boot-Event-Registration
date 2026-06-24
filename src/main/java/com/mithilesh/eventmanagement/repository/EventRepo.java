package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Events,Long> {

    List<Events> findByEventNameContainingIgnoreCase(String name);
}
