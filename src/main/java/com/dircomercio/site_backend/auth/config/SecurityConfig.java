package com.dircomercio.site_backend.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dircomercio.site_backend.auth.repository.Token;
import com.dircomercio.site_backend.auth.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para simplificar (considerar habilitar en producción) 
            .authorizeHttpRequests(req -> 
            req
                // Endpoints públicos
                .requestMatchers(/* "/rol/**", "/auth/register",*/"/expediente/traerEstados/{nroExp}", "/auth/login", "/auth/logout", "/auth/refresh", "/denuncia/subirDenuncia", "/denuncia/traerDenunciaPorExp", "/auth/register").permitAll()
                // Endpoints de mesa de entrada (y admin)
                .requestMatchers(
                    "/denuncia/traerDenuncia",
                    "/denuncia/traerDenunciaPorId/{id}",
                    "/denuncia/actualizarEstado/{id}",
                    "doc/traerPorDenuncia/{id}", "doc/traerPorId/{id}", "/usuarios/perfilUsuario", "/usuarios/actualizarNombre", "/usuarios/cambiarPassword", "/usuarios/traerUsuarios"
                ).hasAnyRole("MESA_ENTRADA", "ADMIN", "ABOGADO")
                // Solo admin puede eliminar usuarios
                .requestMatchers(HttpMethod.DELETE, "/usuarios/borrar/**").hasRole("ADMIN")
                // Solo admin puede registrar
                .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")
                .requestMatchers("/expediente/traerPorUsuario", "/expediente/traerExpedientePorId/{id}", "/pases/**", "/pases/traerPasesPorExp/{id}", "/audiencias/**").hasAnyRole("ADMIN", "ABOGADO")
                // Todo lo demás solo admin
                .anyRequest().hasRole("ADMIN"))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout ->
                logout.logoutUrl("/auth/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                        logout(authHeader);
                    })
                    .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        return http.build();
    }

    private void logout(final String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token invalido");
        }

        final String jwtToken = token.substring(7);
        final Token foundToken = tokenRepository.findByToken(jwtToken);
        if (foundToken == null) {
            throw new IllegalArgumentException("Token invalido");
        }
        foundToken.setExpired(true);
        foundToken.setRevoked(true);
        tokenRepository.save(foundToken);
    }
}
