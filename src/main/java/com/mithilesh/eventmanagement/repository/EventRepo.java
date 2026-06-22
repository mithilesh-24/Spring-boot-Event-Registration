package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Events,Long> {
}
