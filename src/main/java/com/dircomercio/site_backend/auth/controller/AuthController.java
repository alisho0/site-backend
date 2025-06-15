package com.dircomercio.site_backend.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request) {
        try {
            System.out.println(request.email() + " - " + request.password() + " - " + request.name());
            final TokenResponse token = service.register(request);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request) {
        try {
            final TokenResponse token = service.login(request);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace(); // Log completo en consola
            return ResponseEntity.status(403).body(null); // Devuelve 403 explícito
        }  
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        final TokenResponse token = service.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }
}
