package com.dircomercio.site_backend.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {

    /**
     * es para obtener la ip real del cliente
     * verifica primero los headers de proxy y dsp la ip directa
     */

    public static String obtenerIP(HttpServletRequest request) {
        // navegadores/proxies envian ip en este header
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // si hay varias ip toma la primera
            return ip.split(",")[0];
        }

        // sino, se intenta en este header
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // si no se encuentra en headers, se usa la ip directa del cliente
        return request.getRemoteAddr();
    }
}