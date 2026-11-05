package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration,Long> {
    void deleteByEvent(Events events);
}
