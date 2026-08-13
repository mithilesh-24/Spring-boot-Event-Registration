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

    /**
     * generate the jwt token
     *
     * @param email which is required for creating token
     * @return call the createToken func it will return the token
     */
    public  String generateKey(String email){
        HashMap<String,Object> claims = new HashMap<>();
        return createToken(claims,email);
    }

    /**
     *
     * @param claims additonal infomation about the user
     * @param email user identity
     * @return token is return as respond
     */
    public String createToken(HashMap<String,Object> claims,String email){
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 30))
                .signWith(getKey())
                .compact();
    }

    /**
     * decode the secret key to bytes
     *
     * @return the HMAC key used for signing and verifying JWTs
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * From the token extract the email
     *
     * @param token jwt token
     * @return the email for checking
     */
    public String extractEmail(String token){
        return extractClaims(token,claims -> claims.getSubject());
    }

    /**
     * to validate the expiration of data
     *
     * @param token jwt token
     * @return date for checking the date is expired or not
     */
    public Date extractExpiration(String token){
        return extractClaims(token,claims -> claims.getExpiration());
    }

    /**
     *
     * @param token jwt token
     * @param claimsResolver functional interface which get claims as input and T as output
     * @return Respective response is given
     * @param <T> which can LocalDate, String
     */
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
            Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
    }

    /**
     * Want to extract the content in the token
     *
     * @param token jwt token
     * @return the payload content in the jwt token
     */
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validate the token expiration
     *
     * @param token jwt token
     * @return boolean value
     */
    private boolean isTokenExpired(String token){
        return extractExpiration(token).after(new Date());
    }

    /**
     * To check the email in the token is valid or not
     *
     * @param token jwt token
     * @param userDetails User details which contain the email and password
     * @return boolen value
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String email = userDetails.getUsername();

        return email.equals(extractEmail(token)) && isTokenExpired(token);
    }

}
