package com.dircomercio.site_backend.controllers;

import com.dircomercio.site_backend.dtos.AudienciaCreateDTO;
import com.dircomercio.site_backend.dtos.AudienciaDTO;
import com.dircomercio.site_backend.entities.Audiencia;
import com.dircomercio.site_backend.services.AudienciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("audiencias")
public class AudienciaController {

    @Autowired
    private final AudienciaService audienciaService;

    public AudienciaController(AudienciaService audienciaService) {
        this.audienciaService = audienciaService;
    }

    @PostMapping("/creaAudiencia")
    public ResponseEntity<AudienciaDTO> crearAudiencia(@RequestBody AudienciaCreateDTO dto) {
        Audiencia nueva = audienciaService.crearAudienciaDesdeDTO(dto);
        return ResponseEntity.ok(toDTO(nueva));
    }

    @GetMapping("/obtenerAudiencias")
    public ResponseEntity<List<AudienciaDTO>> obtenerTodas() {
        List<Audiencia> audiencias = audienciaService.obtenerTodasLasAudiencias();
        List<AudienciaDTO> dtos = audiencias.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/obtenerAudiencia/{id}")
    public ResponseEntity<AudienciaDTO> obtenerPorId(@PathVariable Long id) {
        Audiencia audiencia = audienciaService.obtenerAudienciaPorId(id);
        return ResponseEntity.ok(toDTO(audiencia));
    }

    @PutMapping("/actualizarAudiencia{id}")
    public ResponseEntity<AudienciaDTO> actualizar(@PathVariable Long id, @RequestBody AudienciaCreateDTO dto) {
        Audiencia actualizada = audienciaService.actualizarAudienciaDesdeDTO(id, dto);
        return ResponseEntity.ok(toDTO(actualizada));
    }

    @DeleteMapping("/borrarAudiencia/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        audienciaService.eliminarAudiencia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/traeAudiPorExp/{id}")
    public ResponseEntity<List<AudienciaDTO>> traerAudienciasPorExpediente(@PathVariable Long id) {
        List<Audiencia> audiencias = audienciaService.traerAudienciasPorExpediente(id);
        List<AudienciaDTO> dtos = audiencias.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/editarAudi/{id}")
    public ResponseEntity<AudienciaDTO> editarAudiencia(@PathVariable Long id, @RequestBody AudienciaCreateDTO dto) {
        Audiencia actualizada = audienciaService.actualizarAudienciaDesdeDTO(id, dto);
        return ResponseEntity.ok(toDTO(actualizada));
    }

    // Métodos de mapeo
    private AudienciaDTO toDTO(Audiencia audiencia) {
        AudienciaDTO dto = new AudienciaDTO();
        dto.setId(audiencia.getId());
        dto.setFecha(audiencia.getFecha());
        dto.setHora(audiencia.getHora());
        dto.setLugar(audiencia.getLugar());
        if (audiencia.getExpediente() != null) {
            dto.setNroExp(audiencia.getExpediente().getNro_exp());
        }
        if (audiencia.getPersonas() != null) {
            dto.setNombresPersonas(audiencia.getPersonas().stream().map(p -> p.getNombre()).collect(Collectors.toList()));
        }
        return dto;
    }
}
