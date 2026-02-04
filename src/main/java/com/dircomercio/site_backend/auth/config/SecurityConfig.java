package com.dircomercio.site_backend.auth.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.dircomercio.site_backend.auth.redis.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
    private final Environment environment;
    private final RateLimitFilter rateLimitFilter;

    // verifica el modo desarrollo
    private boolean isDevelopmentMode() {
        String[] profiles = environment.getActiveProfiles();
        return Arrays.asList(profiles).contains("development");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = new ArrayList<>();

        // --- DOMINIOS OFICIALES ---
        allowedOrigins.add("https://sde.gob.ar");
        allowedOrigins.add("https://www.sde.gob.ar");

        // --- DOMINIOS DE DESARROLLO (Siempre activos para pruebas) ---
        allowedOrigins.add("http://localhost:5173");
        allowedOrigins.add("http://localhost:5174");
        allowedOrigins.add("http://localhost:8080");
        allowedOrigins.add("http://100.83.50.21:8080");
        allowedOrigins.add("https://homothetic-riotingly-leonora.ngrok-free.dev");

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
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
                .authorizeHttpRequests(req -> req
                        // ✅ Permitir preflight CORS (OPTIONS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints públicos
                        .requestMatchers(
                                "/expediente/traerEstados/{nroExp}",
                                "/test",
                                "/auth/login",
                                "/auth/register",
                                "/auth/logout",
                                "/auth/refresh",
                                "/denuncia/subirDenuncia",
                                "/rol/**")
                        .permitAll()

                        // ✅ PERMISOS DE AUDITORÍA (Logs) - ¡Agregado aquí!
                        // Permite acceso a /auditoria y /logs a ADMIN y DIRECCION
                        .requestMatchers("/api/auditoria/**", "/logs/**").hasAnyRole("ADMIN", "DIRECCION")

                        // Endpoints generales
                        .requestMatchers(
                                "/denuncia/traerDenuncia",
                                "/denuncia/traerDenunciaPorId/{id}",
                                "/denuncia/actualizarEstado/{id}",
                                "/doc/traerPorDenuncia/{id}",
                                "/doc/traerPorId/{id}",
                                "/usuarios/perfilUsuario",
                                "/usuarios/actualizarNombre",
                                "/usuarios/cambiarPassword",
                                "/denuncia/traerDenunciasPorUsuario",
                                "/denuncia/historial/{id}")
                        .hasAnyRole("MESA_DE_ENTRADA", "ABOGADOS", "ADMIN", "DIRECCION")

                        // CRUD de Usuarios
                        .requestMatchers(HttpMethod.GET, "/usuarios/traerUsuarios").hasAnyRole("ADMIN", "DIRECCION")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMIN", "DIRECCION")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasAnyRole("ADMIN", "DIRECCION")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/borrar/**").hasAnyRole("ADMIN", "DIRECCION")

                        // Expedientes y Pases
                        .requestMatchers(
                                "/expediente/traerPorUsuario",
                                "/expediente/traerExpedientePorId/{id}",
                                "/expediente/{id}",
                                "/expediente/traerExpedientes",
                                "/pases/**",
                                "/pases/traerPasesPorExp/{id}",
                                "/audiencias/**",
                                "/doc/traerOrdenesPorExpediente/{expedienteId}",
                                "/doc/eliminarDoc/{id}",
                                "/doc/crearOrden")
                        .hasAnyRole("ADMIN", "ABOGADOS", "DIRECCION")

                        // Cualquier otra petición requiere rol alto
                        .anyRequest().hasAnyRole("DIRECCION", "ADMIN"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/auth/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                            logout(authHeader);
                        })
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    private void logout(final String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return;
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