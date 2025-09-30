package com.aurionpro.bankapp.security;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private String getEncodedSecret() {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, getEncodedSecret())
                .compact();
        System.out.println("JWT Generated: " + token);
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = Jwts.parser()
                    .setSigningKey(getEncodedSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            System.out.println("Extracted Username: " + username);
            return username;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("JWT Malformed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("JWT Parsing Error: " + e.getMessage());
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean expired = isTokenExpired(token);
        boolean valid = username != null && username.equals(userDetails.getUsername()) && !expired;
        System.out.println("JWT Validation: usernameMatch=" + (username != null && username.equals(userDetails.getUsername())) +
                           ", isExpired=" + expired +
                           ", valid=" + valid);
        return valid;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        if (expiration == null) return true;
        boolean expired = expiration.before(new Date());
        System.out.println("Token Expiration: " + expiration + ", Now: " + new Date() + ", isExpired=" + expired);
        return expired;
    }

    private Date extractExpiration(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getEncodedSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (ExpiredJwtException e) {
            System.out.println("Extract Expiration - JWT Expired: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Extract Expiration Error: " + e.getMessage());
        }
        return null;
    }
}
