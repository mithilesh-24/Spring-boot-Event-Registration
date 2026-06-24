package com.mithilesh.eventmanagement.repository;


import com.mithilesh.eventmanagement.entity.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepo extends JpaRepository<States,Long> {

    Optional<States> findByStateName(String stateName);
}
