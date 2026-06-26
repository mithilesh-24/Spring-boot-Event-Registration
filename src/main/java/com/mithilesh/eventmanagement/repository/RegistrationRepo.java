package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepo extends JpaRepository<Registration,Long> {
}
