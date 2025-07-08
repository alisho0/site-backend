package com.dircomercio.site_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @PostMapping("/crearPase")
    public ResponseEntity<?> crearPase(@RequestPart("pase") String paseJson, @RequestPart("file") List<MultipartFile> file) {
        try { 
            ObjectMapper mapper = new ObjectMapper();
            PaseCreateDTO paseCreateDTO = mapper.readValue(paseJson, PaseCreateDTO.class);   
            if (paseCreateDTO != null && paseCreateDTO.getExpedienteId() != null && file != null && !file.isEmpty()) {
                paseService.crearPase(paseCreateDTO, file);
                return ResponseEntity.ok().body("El pase fue creado correctamente...");
            }
            return ResponseEntity.badRequest().body("El pase no fue creado, verifique los datos ingresados.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al crear el pase: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear el pase: " + e.getMessage());
        }


    }

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
    public ResponseEntity<List<PaseRespuestaDTO>> traerPasesPorExpediente(@PathVariable Long id) throws Exception {
        try {
            return ResponseEntity.ok(paseService.traerPasesPorExpediente(id));
        } catch (Exception e) {
            throw new Exception("Excepcion:" + e.getMessage());
        }
    }

    @PutMapping("/editarPase/{id}")
    public ResponseEntity<PaseRespuestaDTO> editarPase(@PathVariable Long id, @RequestBody PaseCreateDTO dto) {
        return ResponseEntity.ok(paseService.editarPase(id, dto));
    }
}
