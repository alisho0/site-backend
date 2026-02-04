package com.dircomercio.site_backend.auth.redis;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtro de Rate Limiting simple sin dependencias externas.
 * Limita a 10 peticiones por minuto por IP.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int LIMIT = 10; // Máximo de peticiones por ventana
    private static final long WINDOW_MS = 60_000; // 1 minuto en milisegundos

    // Mapa de IP -> Bucket de peticiones
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Clase interna para almacenar el contador y timestamp de cada IP
     */
    private static class Bucket {
        int count;
        long windowStart;

        Bucket() {
            this.count = 1;
            this.windowStart = System.currentTimeMillis();
        }

        /**
         * Intenta incrementar el contador si está dentro de la ventana de tiempo
         * 
         * @return true si se permite la petición, false si se excedió el límite
         */
        synchronized boolean tryIncrement() {
            long now = System.currentTimeMillis();

            // Si la ventana expiró, resetear el bucket
            if (now - windowStart > WINDOW_MS) {
                count = 1;
                windowStart = now;
                return true;
            }

            // Si estamos dentro de la ventana, verificar el límite
            if (count < LIMIT) {
                count++;
                return true;
            }

            // Límite excedido
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();

        // Excluir solo preflight OPTIONS (CORS)
        if (method.equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener IP del cliente
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        // Tomar solo la primera IP si hay múltiples (proxy chain)
        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // Verificar rate limit
        Bucket bucket = buckets.computeIfAbsent(ip, k -> new Bucket());

        if (!bucket.tryIncrement()) {
            // Límite excedido - responder con 429
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Demasiadas peticiones. Intente nuevamente en unos segundos.");
            return;
        }

        // Limpieza periódica de buckets expirados (cada 100 peticiones)
        if (Math.random() < 0.01) {
            cleanupExpiredBuckets();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Limpia buckets que han expirado para evitar memory leak
     */
    private void cleanupExpiredBuckets() {
        long now = System.currentTimeMillis();
        buckets.entrySet().removeIf(entry -> now - entry.getValue().windowStart > WINDOW_MS * 2);
    }
}
