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
import com.dircomercio.site_backend.entities.Area;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.entities.Rol;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.PersonaRepository;
import com.dircomercio.site_backend.repositories.RolRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
import com.dircomercio.site_backend.services.AuditoriaService;
import com.dircomercio.site_backend.utils.IpUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RolRepository rolRepository;
    private final PersonaRepository personaRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    // registrar auditoria en registro
    public TokenResponse register(RegisterRequest request, HttpServletRequest servletRequest) {
        Area area;
        try {
            area = Area.valueOf(request.rol().toUpperCase());
        } catch (Exception e) {
            auditoriaService.registrarAccion(request.email(), "REGISTER_FAILED", "Rol invalido: " + request.rol(), 
                IpUtil.obtenerIP(servletRequest), "FAILURE", "Usuario", null);
            throw new IllegalArgumentException("Rol no válido: " + request.rol());
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            auditoriaService.registrarAccion(request.email(), "REGISTER_FAILED", "Email ya en uso: " + request.email(), 
                IpUtil.obtenerIP(servletRequest), "FAILURE", "Usuario", null);
            throw new IllegalArgumentException("El email ya está en uso: " + request.email());
        }

        Persona personaExistente = personaRepository.findByDocumento(request.documento()).orElse(null);
        if (personaExistente != null) {
            Usuario user = Usuario.builder()
                .nombre(request.name())
                .email(request.email())
                .contraseña(passwordEncoder.encode(request.password()))
                .rol(area)
                .persona(personaExistente)
                .build();
            Usuario savedUser = usuarioRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            auditoriaService.registrarAccion(request.email(), "REGISTER_SUCCESS", "Usuario registrado: " + request.email(), 
                IpUtil.obtenerIP(servletRequest), "SUCCESS", "Usuario", savedUser.getId());
            return new TokenResponse(jwtToken, refreshToken);
        }

        Persona persona = Persona.builder()
            .nombre(request.nombre())
            .apellido(request.apellido())
            .email(request.email())
            .telefono(request.telefono())
            .cp(request.cp())
            .localidad(request.localidad())
            .documento(request.documento())
            .domicilio(request.domicilio())
            .build();

        Usuario user = Usuario.builder()
            .nombre(request.name())
            .email(request.email())
            .contraseña(passwordEncoder.encode(request.password()))
            .rol(area)
            .persona(persona)
            .build();
            
        personaRepository.save(persona);
        Usuario savedUser = usuarioRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        auditoriaService.registrarAccion(request.email(), "REGISTER_SUCCESS", "Usuario registrado: " + request.email(), 
            IpUtil.obtenerIP(servletRequest), "SUCCESS", "Usuario", savedUser.getId());
        return new TokenResponse(jwtToken, refreshToken);
    }

    public void saveUserToken(Usuario user, String jwtToken) {
        Token token = Token.builder()
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

    // registrar auditoria en login
    public TokenResponse login(LoginRequest request, HttpServletRequest servletRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(), 
                    request.password()
                )
            );
        } catch (Exception e) {
            auditoriaService.registrarAccion(request.email(), "LOGIN_FAILED", "Credenciales invalidas", 
                IpUtil.obtenerIP(servletRequest), "FAILURE", "Usuario", null);
            throw e;
        }
        Usuario user = usuarioRepository.findByEmail(request.email())
            .orElseThrow(() -> new UsernameNotFoundException("No encontrado el usuario"));
        if (user == null) {
            auditoriaService.registrarAccion(request.email(), "LOGIN_FAILED", "Usuario no encontrado", 
                IpUtil.obtenerIP(servletRequest), "FAILURE", "Usuario", null);
            throw new RuntimeException("Usuario no encontrado con email: " + request.email());
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        auditoriaService.registrarAccion(request.email(), "LOGIN_SUCCESS", "Sesion iniciada: " + request.email(), 
            IpUtil.obtenerIP(servletRequest), "SUCCESS", "Usuario", user.getId());
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

    // registrar auditoria en logout
    public void logout(String authHeader, HttpServletRequest servletRequest) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no proporcionado o formato incorrecto");
        }
        String jwt = authHeader.substring(7);
        Token token = tokenRepository.findByToken(jwt);
        if (token == null) {
            throw new IllegalArgumentException("Token no encontrado");
        }
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
        auditoriaService.registrarAccion(token.getUser().getEmail(), "LOGOUT_SUCCESS", "Sesion cerrada", 
            IpUtil.obtenerIP(servletRequest), "SUCCESS", "Usuario", token.getUser().getId());
    }
}
