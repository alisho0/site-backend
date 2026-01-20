
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

        // se crea una lista dinamicamente de orígenes permitidos
        List<String> allowedOrigins = new ArrayList<>();

        // estos son lo dominios oficiales (SIEMPRE activos en cualquier ambiente)
        allowedOrigins.add("https://sde.gob.ar");
        allowedOrigins.add("https://www.sde.gob.ar");

        // y estos lo dominios de desarrollo (SOLO cuando
        // spring.profiles.active=development)
        if (isDevelopmentMode()) {
            allowedOrigins.add("http://localhost:5173");
            allowedOrigins.add("http://localhost:5174");
            allowedOrigins.add("100.83.50.21:8080");
            allowedOrigins.add("http://100.83.50.21:8080");
            allowedOrigins.add("https://homothetic-riotingly-leonora.ngrok-free.dev");
        }

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
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
                        // ✅ IMPORTANTE: permitir preflight CORS (OPTIONS) para todas las rutas
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

                        // Endpoints generales para usuarios logueados
                        .requestMatchers(
                                "/denuncia/traerDenuncia",
                                "/denuncia/traerDenunciaPorId/{id}",
                                "/denuncia/actualizarEstado/{id}",
                                "doc/traerPorDenuncia/{id}",
                                "doc/traerPorId/{id}",
                                "/usuarios/perfilUsuario",
                                "/usuarios/actualizarNombre",
                                "/usuarios/cambiarPassword",
                                "/denuncia/traerDenunciasPorUsuario",
                                "/denuncia/historial/{id}")
                        .hasAnyRole("MESA_DE_ENTRADA", "ABOGADOS", "ADMIN", "DIRECCION")

                        // --- REGLAS ESPECÍFICAS PARA EL CRUD DE USUARIOS ---
                        .requestMatchers(HttpMethod.GET, "/usuarios/traerUsuarios").hasAnyRole("ADMIN", "DIRECCION")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMIN", "DIRECCION") // Ver
                                                                                                            // detalles
                        .requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasAnyRole("ADMIN", "DIRECCION") // Editar
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/borrar/**").hasAnyRole("ADMIN", "DIRECCION") // Borrar

                        // Reglas para Expedientes, Pases, etc.
                        .requestMatchers(
                                "/expediente/traerPorUsuario",
                                "/expediente/traerExpedientePorId/{id}",
                                "/pases/**",
                                "/pases/traerPasesPorExp/{id}",
                                "/audiencias/**",
                                "/doc/traerOrdenesPorExpediente/{expedienteId}",
                                "/doc/eliminarDoc/{id}",
                                "/doc/crearOrden")
                        .hasAnyRole("ADMIN", "ABOGADOS", "DIRECCION")

                        // Regla final: cualquier otra petición requiere ser ADMIN o DIRECCION
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
