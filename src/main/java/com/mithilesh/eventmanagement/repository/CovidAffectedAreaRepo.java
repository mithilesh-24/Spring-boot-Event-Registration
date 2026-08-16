package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.CovidAffectedArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CovidAffectedAreaRepo extends JpaRepository<CovidAffectedArea,Long> {
}
