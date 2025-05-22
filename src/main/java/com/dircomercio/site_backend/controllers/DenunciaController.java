package com.dircomercio.site_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/denuncia")
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    DocumentoService documentoService;

    @PostMapping("/subirDenuncia")
    public ResponseEntity<?> subirDenuncia(
            @RequestPart("denuncia") String denunciaJson,
            @RequestPart("file") List<MultipartFile> files) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DenunciaDTO denunciaDTO = mapper.readValue(denunciaJson, DenunciaDTO.class);
            denunciaService.guardarDenuncia(denunciaDTO, files);
            System.out.println("Denuncia: " + denunciaDTO);
            System.out.println("Personas: " + denunciaDTO.getPersonas());
            return ResponseEntity.ok().body("La denuncia fue subida correctamente desde el controlador.");
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Algo salió mal en el controlador");
        }
    }

    @GetMapping("/traerDenuncia")
    public ResponseEntity<?> traerDenuncia() {
        try {
            return ResponseEntity.ok().body(denunciaService.traerDenuncias());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo traer la denuncia.");
        }
    }
    
}
