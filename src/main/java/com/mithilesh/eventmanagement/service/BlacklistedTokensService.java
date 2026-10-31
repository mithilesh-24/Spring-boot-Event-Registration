package com.mithilesh.eventmanagement.service;

import com.mithilesh.eventmanagement.entity.BlacklistedTokens;
import com.mithilesh.eventmanagement.repository.BlacklistedTokensRepo;
import com.mithilesh.eventmanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
        Instant expireAt = jwtService.extractExpiration(token).toInstant();

        BlacklistedTokens blacklistedTokens = new BlacklistedTokens();
        blacklistedTokens.setExpireAt(expireAt);
        blacklistedTokens.setToken(token);

        blacklistedTokensRepo.save(blacklistedTokens);
    }

    public boolean isBlacklisted(String token){

        return blacklistedTokensRepo.existsByToken(token);
    }

    public void cleanUp(){
        blacklistedTokensRepo.deleteByExpireAtBefore(Instant.now());
    }
}
