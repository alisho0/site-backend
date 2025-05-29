package com.dircomercio.site_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.services.ExpedienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/expediente")
public class ExpedienteController {

    @Autowired
    private ExpedienteService expedienteService;

    @PostMapping
    public ResponseEntity<?> crearExpediente(@Valid @RequestBody ExpedienteCreateDTO dto) {
        try {
            Expediente expediente = expedienteService.crearExpedienteDesdeDTO(dto);
            return ResponseEntity.status(201).body(expediente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al crear el expediente: " + e.getMessage());
        }
    }
}

