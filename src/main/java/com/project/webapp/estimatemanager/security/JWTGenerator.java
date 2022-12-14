package com.project.webapp.estimatemanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.project.webapp.estimatemanager.security.SecurityConstants.JWT_EXPIRATION;
import static com.project.webapp.estimatemanager.security.SecurityConstants.JWT_SECRET;

@Component
public class JWTGenerator {
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

public String getUsernameFromJWT(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(JWT_SECRET)
            .parseClaimsJws(token)
            .getBody();
    return claims.getSubject();
}

public boolean validateToken(String token) {
    try {
        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
        return true;
    } catch (Exception e) {
        throw new AuthenticationCredentialsNotFoundException("JWT era scaduto o sbagliato");
    }
}
}
