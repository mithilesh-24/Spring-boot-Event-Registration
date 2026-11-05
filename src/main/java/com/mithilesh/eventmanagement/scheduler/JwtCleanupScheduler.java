package com.mithilesh.eventmanagement.scheduler;

import com.mithilesh.eventmanagement.entity.BlacklistedTokens;
import com.mithilesh.eventmanagement.repository.BlacklistedTokensRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtCleanupScheduler {
    private final BlacklistedTokensRepo blacklistedTokensRepo;

    @Scheduled(fixedDelay = 9_00_000)
    @Transactional
    public void cleanExpiredBlacklistTokens(){
        log.info("Starting Cleaning Expired Blacklist Tokens");

        int deletedCount = blacklistedTokensRepo.deleteByExpireAtBefore(Instant.now());

        log.info("Deleted {} expired blacklist tokens completed successfully", deletedCount);
    }
}
