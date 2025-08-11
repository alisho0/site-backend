package com.dircomercio.site_backend.auth.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.entities.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String generateToken(Usuario user) {
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(Usuario user) {
        return buildToken(user, refreshExpiration);
    }

    // Aquí construye el token, para registrar y loguear, lo usan los dos
    private String buildToken(final Usuario user, final long expiration) {
        return Jwts.builder()
            .id(user.getId().toString())
            .claims(Map.of("name", user.getNombre()))
            .subject(user.getEmail())
            .claim("rol", user.getRol() != null ? user.getRol().name() : "USER")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey())
            .compact();
    }

    // Decodifica la key y la pasa al algoritmo hmac
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(final String token) {
        final Claims claims = Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.getSubject();
    }

    public String extractRol(final String token) {
        final Claims claims = Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.get("rol", String.class);
    }

    public boolean isTokenValid(final String token, final Usuario user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Claims claims = Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.getExpiration().before(new Date());
    }
}
