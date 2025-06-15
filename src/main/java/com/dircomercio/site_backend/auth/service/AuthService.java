package com.dircomercio.site_backend.auth.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.auth.controller.LoginRequest;
import com.dircomercio.site_backend.auth.controller.RegisterRequest;
import com.dircomercio.site_backend.auth.controller.TokenResponse;
import com.dircomercio.site_backend.auth.repository.Token;
import com.dircomercio.site_backend.auth.repository.TokenRepository;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {
        var user = Usuario.builder()
            .nombre(request.name())
            .email(request.email())
            .contraseña(passwordEncoder.encode(request.password()))
            .build();

        var savedUser = usuarioRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public void saveUserToken(Usuario user, String jwtToken) {
        var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(Token.TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    public TokenResponse refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no proporcionado o formato incorrecto");
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        final Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("No fue encontrado el usuario"));
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con email: " + userEmail);
        };

        if (!jwtService.isTokenValid(refreshToken, usuario)) {
            throw new RuntimeException("Token no válido o expirado");
        }

        final String accessToken = jwtService.generateToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, accessToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(), 
                request.password()
            )
        );
        var user = usuarioRepository.findByEmail(request.email())
            .orElseThrow(() -> new UsernameNotFoundException("No encontrado el usuario"));
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado con email: " + request.email());
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    private void revokeAllUserTokens(final Usuario user) {
        final List<Token> validUserTokens = tokenRepository.findAllByUserIdAndExpiredFalseAndRevokedFalse(user.getId());
        if (!validUserTokens.isEmpty()) {
            for (final Token token : validUserTokens) {
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }

    
}
