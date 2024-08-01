package com.nothing.stella.services;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(UserDetails user);

    String fetchReference(String token);

    Date fetchExpirationDate(String token);

    Boolean isTokenExpired(String token);

    Boolean validateToken(String token, UserDetails user);

    Boolean verifyJwtHeader(String header);
}
