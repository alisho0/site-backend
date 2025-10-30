package com.dircomercio.site_backend.auth.config;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:8080", "100.83.50.21:8080", "http://100.83.50.21:8080", "https://homothetic-riotingly-leonora.ngrok-free.dev"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> 
            req
                // Endpoints públicos
                .requestMatchers("/expediente/traerEstados/{nroExp}", "/auth/login", "/auth/register", "/auth/logout", "/auth/refresh", "/denuncia/subirDenuncia", "/rol/**").permitAll()
                
                // Endpoints generales para usuarios logueados
                .requestMatchers(
                    "/denuncia/traerDenuncia",
                    "/denuncia/traerDenunciaPorId/{id}",
                    "/denuncia/actualizarEstado/{id}",
                    "doc/traerPorDenuncia/{id}", "doc/traerPorId/{id}", "/usuarios/perfilUsuario", "/usuarios/actualizarNombre", "/usuarios/cambiarPassword", "/denuncia/traerDenunciasPorUsuario", "/denuncia/historial/{id}"
                ).hasAnyRole("MESA_DE_ENTRADA", "ABOGADOS", "ADMIN", "DIRECCION")

                // --- REGLAS ESPECÍFICAS PARA EL CRUD DE USUARIOS ---
                .requestMatchers(HttpMethod.GET, "/usuarios/traerUsuarios").hasAnyRole("ADMIN", "DIRECCION")
                .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMIN", "DIRECCION") // Ver detalles
                .requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasAnyRole("ADMIN", "DIRECCION") // Editar
                .requestMatchers(HttpMethod.DELETE, "/usuarios/borrar/**").hasAnyRole("ADMIN", "DIRECCION") // Borrar

                // Reglas para Expedientes, Pases, etc.
                .requestMatchers("/expediente/traerPorUsuario", "/expediente/traerExpedientePorId/{id}", "/pases/**", "/pases/traerPasesPorExp/{id}", "/audiencias/**", "/doc/traerOrdenesPorExpediente/{expedienteId}", "/doc/eliminarDoc/{id}", "/doc/crearOrden").hasAnyRole("ADMIN", "ABOGADOS", "DIRECCION")
                
                // Regla final: cualquier otra petición requiere ser ADMIN o DIRECCION
                .anyRequest().hasAnyRole("DIRECCION", "ADMIN"))
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
            return; // No hacer nada si no hay token
        }
        final String jwtToken = token.substring(7);
        final Token foundToken = tokenRepository.findByToken(jwtToken);
        if (foundToken != null) {
            foundToken.setExpired(true);
            foundToken.setRevoked(true);
            tokenRepository.save(foundToken);
        }
    }
}

