package com.dircomercio.site_backend.controllers;

import com.dircomercio.site_backend.entities.Audiencia;
import com.dircomercio.site_backend.services.AudienciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audiencias")
public class AudienciaController {

    private final AudienciaService audienciaService;

    @Autowired
    public AudienciaController(AudienciaService audienciaService) {
        this.audienciaService = audienciaService;
    }

    @PostMapping
    public ResponseEntity<Audiencia> crearAudiencia(@RequestBody Audiencia audiencia) {
        Audiencia nueva = audienciaService.crearAudiencia(audiencia);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping
    public ResponseEntity<List<Audiencia>> obtenerTodas() {
        return ResponseEntity.ok(audienciaService.obtenerTodasLasAudiencias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Audiencia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(audienciaService.obtenerAudienciaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Audiencia> actualizar(@PathVariable Long id, @RequestBody Audiencia datosActualizados) {
        return ResponseEntity.ok(audienciaService.actualizarAudiencia(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        audienciaService.eliminarAudiencia(id);
        return ResponseEntity.noContent().build();
    }
}
