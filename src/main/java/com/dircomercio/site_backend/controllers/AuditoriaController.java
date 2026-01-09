package com.dircomercio.site_backend.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.entities.AuditoriaLog;
import com.dircomercio.site_backend.services.AuditoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {
    
    private final AuditoriaService auditoriaService;
    
    // Obtener todos los logs (solo ADMIN)
    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerTodosLosLogs() {
        return ResponseEntity.ok(auditoriaService.obtenerTodosLosLogs());
    }
    
    // Obtener logs de un usuario
    @GetMapping("/logs/usuario/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerLogsPorUsuario(@PathVariable String email) {
        List<AuditoriaLog> logs = auditoriaService.obtenerLogsPorUsuario(email);
        return ResponseEntity.ok(logs);
    }
    
    // Obtener logs fallidos (intentos de acceso no autorizados)
    @GetMapping("/logs/fallidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerLogsFallidos() {
        List<AuditoriaLog> logs = auditoriaService.obtenerLogsFallidos();
        return ResponseEntity.ok(logs);
    }
    
    // Obtener logs de un rango de fechas
    @GetMapping("/logs/fecha")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerLogsPorFecha(
        @RequestParam LocalDateTime inicio,
        @RequestParam LocalDateTime fin
    ) {
        List<AuditoriaLog> logs = auditoriaService.obtenerLogsPorFecha(inicio, fin);
        return ResponseEntity.ok(logs);
    }
}