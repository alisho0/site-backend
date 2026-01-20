package com.dircomercio.site_backend.auth.redis;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimitService rateLimitService;
    private static final int LIMIT = 100;  // de 10 a 100
    private static final Duration WINDOW = Duration.ofSeconds(60);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String path = request.getRequestURI();

        // Excluir preflight OPTIONS y endpoints de autenticación
        if (method.equals("OPTIONS") || path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty()) {
                ip = request.getRemoteAddr();
            }
            // Tomar solo la primera IP si hay múltiples
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }

            String key = "rate:" + ip + ":" + path;

            if (!rateLimitService.allowRequest(key)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests...");
                return;
            }
        } catch (Exception e) {
            // Si Redis no está disponible, log pero permitir la request
            System.err.println("[RateLimitFilter] Warning: Redis unavailable, allowing request anyway: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

