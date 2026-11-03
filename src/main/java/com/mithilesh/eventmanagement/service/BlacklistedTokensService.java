package com.mithilesh.eventmanagement.service;

import com.mithilesh.eventmanagement.entity.BlacklistedTokens;
import com.mithilesh.eventmanagement.repository.BlacklistedTokensRepo;
import com.mithilesh.eventmanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlacklistedTokensService {
    private final JwtService jwtService;
    private final BlacklistedTokensRepo blacklistedTokensRepo;

    /**
     * To add token to the blacklist
     *
     * @param token jwt token
     */
    public void addBlackList(String token){
        log.debug("Add token to the blacklist");
        Instant expireAt = jwtService.extractExpiration(token).toInstant();

        BlacklistedTokens blacklistedTokens = new BlacklistedTokens();
        blacklistedTokens.setExpireAt(expireAt);
        blacklistedTokens.setToken(token);

        blacklistedTokensRepo.save(blacklistedTokens);
        log.debug("Token added to blacklist");
    }

    public boolean isBlacklisted(String token){
        log.debug("check Token is Blacklisted or not");
        return blacklistedTokensRepo.existsByToken(token);
    }
}
