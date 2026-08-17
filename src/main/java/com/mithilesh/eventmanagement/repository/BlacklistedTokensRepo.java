package com.mithilesh.eventmanagement.repository;

import com.mithilesh.eventmanagement.entity.BlacklistedTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface BlacklistedTokensRepo extends JpaRepository<BlacklistedTokens,Long> {
    boolean existsByToken(String token);

    void deleteByExpireAtBefore(Instant expireAtBefore);
}
