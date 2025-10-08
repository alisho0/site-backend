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

    /*                 registry.addMapping("/**")
                    .allowedOrigins("http://localhost:8080", "http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "https://site-backend-f8xg.onrender.com", "https://frontend-site-theta.vercel.app")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
                    */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*")); // Esto permite todos los headers
        configuration.setAllowCredentials(true); // Si se necesita enviar a futuro cookies o headers de autenticación
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Primero permite todas las rutas para desarrollo y después le pasa toda la configuración de CORS
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para simplificar (considerar habilitar en producción) 
            .authorizeHttpRequests(req -> 
            req
                // Endpoints públicos
                .requestMatchers("/expediente/traerEstados/{nroExp}", "/auth/login", "/auth/logout", "/auth/refresh", "/denuncia/subirDenuncia", "/auth/register", "/rol/**").permitAll()
                // Endpoints de mesa de entrada, abogado
                .requestMatchers(
                    "/denuncia/traerDenuncia",
                    "/denuncia/traerDenunciaPorId/{id}",
                    "/denuncia/actualizarEstado/{id}",
                    "doc/traerPorDenuncia/{id}", "doc/traerPorId/{id}", "/usuarios/perfilUsuario", "/usuarios/actualizarNombre", "/usuarios/cambiarPassword", "/denuncia/traerDenunciasPorUsuario", "/denuncia/historial/{id}"
                ).hasAnyRole("MESA_DE_ENTRADA", "ABOGADO", "ADMIN", "DIRECCION")
                // Solo admin puede eliminar usuarios
                .requestMatchers(HttpMethod.DELETE, "/usuarios/borrar/**").hasAnyRole("ADMIN", "DIRECCION")
                // Solo admin puede registrar
                .requestMatchers(HttpMethod.POST, "/auth/register").hasAnyRole("ADMIN", "DIRECCION")
                .requestMatchers("/expediente/traerPorUsuario", "/expediente/traerExpedientePorId/{id}", "/pases/**", "/pases/traerPasesPorExp/{id}", "/audiencias/**", "/doc/traerOrdenesPorExpediente/{expedienteId}", "/doc/eliminarDoc/{id}", "/doc/crearOrden").hasAnyRole("ADMIN", "ABOGADO", "DIRECCION")
                // Todo lo demás solo admin
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
