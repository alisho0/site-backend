package com.dircomercio.site_backend.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request, HttpServletRequest servletRequest) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            final TokenResponse token = service.register(request, servletRequest);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request, HttpServletRequest servletRequest) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("[DEBUG] Authorities en el método: " + auth.getAuthorities());
        final TokenResponse token = service.login(request, servletRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        final TokenResponse token = service.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, HttpServletRequest servletRequest) {
        try {
            service.logout(authHeader, servletRequest);
            return ResponseEntity.ok("Sesión cerrada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cerrar sesión: " + e.getMessage());
        }
    }
}
