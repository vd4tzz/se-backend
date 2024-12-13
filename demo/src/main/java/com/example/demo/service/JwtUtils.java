package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.security.MessageDigest;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;




@Service
public class JwtUtils {
    private final int expiration = 60 * 60 * 1000;
    // private final int expiration = 9 * 60 * 1000;
    private String secretKeyStr = "ridosiuuuuuuu";
    private final byte[] secretKey; 

    public JwtUtils() {
        byte[] tmp = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA256");
            tmp = digest.digest(secretKeyStr.getBytes());

        } catch(NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        secretKey = (tmp != null )? tmp : null;

    }

    private SecretKey getKey() {
        SecretKey sk = new SecretKeySpec(secretKey, "HmacSHA256");

        return sk;
    }
    
    public String generateToken(String username, String role) {

        Date current = new Date(System.currentTimeMillis());
        

        String token = Jwts.builder()
            .header()
                .add("typ", "JWT")
            .and()
            .claim("role", role)
            .claims()
                .subject(username)
                .issuedAt(current)
                .expiration(new Date(current.getTime() + expiration))
            .and()
            .signWith(getKey())
            .compact();
        
            
        
        return token;
    }

    public String extractUsername(String token) throws JwtException {
        String username = null;
        Jws<Claims> parsed = null; 

        parsed = validateToken(token);

        username = parsed.getPayload().getSubject();
        
        return username;
    }

    public Jws<Claims> validateToken(String token) throws JwtException {
        return Jwts.parser()
                   .verifyWith(getKey()) 
                   .build()
                   .parseSignedClaims(token);
    }

    public boolean isUnder10Mins(String token) {
        System.out.println("Under 10 mins");
        Claims claims =Jwts.parser()
                            .verifyWith(getKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload(); 

        Date issue = claims.getIssuedAt();
        Date exp   = claims.getExpiration();

        long timeRemaining = exp.getTime() - issue.getTime();
        
        return timeRemaining <= 10 * 60 * 1000; 
    }
}
