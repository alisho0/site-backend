package com.dircomercio.site_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType; // CAMBIO: Import necesario
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.PaseCreateDTO;
import com.dircomercio.site_backend.dtos.PaseRespuestaDTO;
import com.dircomercio.site_backend.services.PaseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/pases")
public class PaseController {
    @Autowired
    PaseService paseService;
    
    // Este método ya estaba bien, no se toca
    @PostMapping("/crearPase")
    public ResponseEntity<?> crearPase(@RequestPart("pase") String paseJson, @RequestPart("file") List<MultipartFile> file) {
        try { 
            ObjectMapper mapper = new ObjectMapper();
            PaseCreateDTO paseCreateDTO = mapper.readValue(paseJson, PaseCreateDTO.class);   
            // Se asume que el servicio maneja el caso de 'file' vacío si es opcional
            paseService.crearPase(paseCreateDTO, file);
            return ResponseEntity.ok().body("El pase fue creado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al crear el pase: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear el pase: " + e.getMessage());
        }
    }

    // El resto de los métodos GET y DELETE no se tocan
    @GetMapping("/traerPases")
    public ResponseEntity<List<PaseRespuestaDTO>> traerPases() {
        return ResponseEntity.ok(paseService.traerPases());
    }

    @GetMapping("/traerPasePorId/{id}")
    public ResponseEntity<?> traerPasePorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(paseService.traerPasePorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se encontró el pase con ID: " + id);
        }
    }

    @DeleteMapping("/borrarPase/{id}")
    public ResponseEntity<?> eliminarPase(@PathVariable Long id) {
        try {
            paseService.eliminarPase(id);
            return ResponseEntity.ok("Pase eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo eliminar el pase: " + e.getMessage());
        }
    }

    @GetMapping("/traerPasesPorExp/{id}")
    public ResponseEntity<List<PaseRespuestaDTO>> traerPasesPorExpediente(@PathVariable Long id) {
        // Es mejor no lanzar una excepción genérica aquí, sino manejarla
        try {
            return ResponseEntity.ok(paseService.traerPasesPorExpediente(id));
        } catch (Exception e) {
            // Devolver un error más informativo en el cuerpo de la respuesta
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ CAMBIO CLAVE: MÉTODO EDITAR CORREGIDO
    @PutMapping(value = "/editarPase/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> editarPase(
        @PathVariable Long id, 
        @RequestPart("pase") String paseJson, 
        // Se marca el archivo como no requerido (required = false)
        @RequestPart(value = "file", required = false) List<MultipartFile> file
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PaseCreateDTO paseCreateDTO = mapper.readValue(paseJson, PaseCreateDTO.class);
            
            // Llamamos al servicio con los parámetros correctos
            PaseRespuestaDTO paseActualizado = paseService.editarPase(id, paseCreateDTO, file);
            return ResponseEntity.ok(paseActualizado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al editar el pase: " + e.getMessage());
        }
    }
}
