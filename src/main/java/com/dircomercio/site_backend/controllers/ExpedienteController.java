package com.dircomercio.site_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.dtos.ExpedienteCreateMinimalDTO;
import com.dircomercio.site_backend.dtos.ExpedienteIdRespuestaDTO;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.services.ExpedienteService;

@RestController
@RequestMapping("/expediente")
public class ExpedienteController {

    @Autowired
    private ExpedienteService expedienteService;

    // Crear expediente con datos mínimos
    @PostMapping("/minimal")
    public ResponseEntity<Expediente> crearExpedienteMinimal(@RequestBody ExpedienteCreateMinimalDTO dto) throws Exception {
        Expediente nuevo = expedienteService.crearExpedienteDesdeMinimalDTO(dto);
        return ResponseEntity.ok(nuevo);
    }

    // Crear expediente desde una denuncia aceptada
    @PostMapping("/desde-denuncia/{denunciaId}")
    public ResponseEntity<Expediente> crearExpedienteDesdeDenuncia(@PathVariable Long denunciaId) throws Exception {
        Expediente nuevo = expedienteService.crearExpedienteDesdeDenuncia(denunciaId);
        return ResponseEntity.ok(nuevo);
    }

    // Crear expediente completo
    @PostMapping
    public ResponseEntity<Expediente> crearExpediente(@RequestBody ExpedienteCreateDTO dto) {
        Expediente nuevo = expedienteService.crearExpedienteDesdeDTO(dto);
        return ResponseEntity.ok(nuevo);
    }

    // Obtener expediente por ID
    @GetMapping("/traerExpedientePorId/{id}")
    public ResponseEntity<ExpedienteIdRespuestaDTO> obtenerExpedientePorId(@PathVariable Long id) {
        ExpedienteIdRespuestaDTO expediente = expedienteService.traerExpedientePorId(id);
        return ResponseEntity.ok().body(expediente);
    }

    // Listar todos los expedientes
    @GetMapping("/traerExpedientes")
    public ResponseEntity<?> listarExpedientes() {
        return ResponseEntity.ok(expedienteService.listarExpedientes());
    }

    // Actualizar expediente
    @PutMapping("/{id}")
    public ResponseEntity<Expediente> actualizarExpediente(@PathVariable Long id, @RequestBody Expediente expedienteActualizado) {
        Expediente actualizado = expedienteService.actualizarExpediente(id, expedienteActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar expediente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarExpediente(@PathVariable Long id) {
        expedienteService.eliminarExpediente(id);
        return ResponseEntity.noContent().build();
    }
}

