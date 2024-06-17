package com.nothing.ecommerce.services;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class JWTServiceImpl implements JWTService {
    // requirement :
    private static final long EXPIRATION_TIME_LIMIT = 24 * 60 * 60 * 1000;

    SecretKey key = Jwts.SIG.HS256.key().build(); // or HS384.key() or HS512.key()

    @Override
    public String generateToken(UserDetails user) {
        // fetch authority/role from user details
        // SimpleGrantedAuthority role = (SimpleGrantedAuthority)
        // user.getAuthorities().toArray()[0];

        return Jwts.builder()
                // .claim("role", role.getAuthority())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + EXPIRATION_TIME_LIMIT))
                .signWith(key)
                .compact();
    }

    @Override
    public String fetchReference(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // public String fetchRole(String token) {
    // return
    // Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("role",
    // String.class);
    // }

    @Override
    public Date fetchExpirationDate(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        Date expiration = fetchExpirationDate(token);
        return expiration.before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails user) {
        return user.getUsername().equals(fetchReference(token))
                && !isTokenExpired(token);
    }

    @Override
    public Boolean verifyJwtHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return true;
        } else {
            return false;
        }
    }
}
