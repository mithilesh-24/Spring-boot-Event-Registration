package com.mithilesh.eventmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public  String generateKey(String email){
        HashMap<String,Object> claims = new HashMap<>();
        return createToken(claims,email);
    }
    public String createToken(HashMap<String,Object> claims,String email){
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 30))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token){
        return extractClaims(token,claims -> claims.getSubject());
    }

    public Date extractExpiration(String token){
        return extractClaims(token,claims -> claims.getExpiration());
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
            Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).after(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String email = userDetails.getUsername();

        return email.equals(extractEmail(token)) && isTokenExpired(token);
    }

}
