package com.mithilesh.eventmanagement.repository;


import com.mithilesh.eventmanagement.entity.States;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepo extends JpaRepository<States,Long> {


    Optional<States> findByStateNameIgnoreCase(@NotBlank(message = "State Name is required") String stateName);
}
