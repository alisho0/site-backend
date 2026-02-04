package com.dircomercio.site_backend.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.entities.AuditoriaLog;
import com.dircomercio.site_backend.services.AuditoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // Obtener todos los logs
    // La seguridad se maneja en SecurityConfig (permite ADMIN y DIRECCION)
    @GetMapping("/logs")
    public ResponseEntity<?> obtenerTodosLosLogs() {
        return ResponseEntity.ok(auditoriaService.obtenerTodosLosLogs());
    }

    // Obtener logs de un usuario específico
    @GetMapping("/logs/usuario/{email}")
    public ResponseEntity<?> obtenerLogsPorUsuario(@PathVariable String email) {
        List<AuditoriaLog> logs = auditoriaService.obtenerLogsPorUsuario(email);
        return ResponseEntity.ok(logs);
    }

    // Obtener logs de intentos fallidos
    @GetMapping("/logs/fallidos")
    public ResponseEntity<?> obtenerLogsFallidos() {
        List<AuditoriaLog> logs = auditoriaService.obtenerLogsFallidos();
        return ResponseEntity.ok(logs);
    }

    // Obtener logs por rango de fechas
    @GetMapping("/logs/fecha")
    public ResponseEntity<?> obtenerLogsPorFecha(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        List<AuditoriaLog> logs = auditoriaService.obtenerLogsPorFecha(inicio, fin);
        return ResponseEntity.ok(logs);
    }
}