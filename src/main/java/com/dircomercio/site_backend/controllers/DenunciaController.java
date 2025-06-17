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
import com.dircomercio.site_backend.dtos.DenunciaUpdateDTO;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/denuncia")
// http://localhost:8080/denuncia/
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

    @GetMapping("/traerDenunciaPorId/{id}")
    public ResponseEntity<?> traerDenunciaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(denunciaService.traerDenunciaPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo traer la denuncia.");
        }
    }

    @PutMapping("/actualizarEstado/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody DenunciaUpdateDTO dto) {
        try {
            denunciaService.actualizarEstadoDenuncia(id, dto);
            return ResponseEntity.ok().body("El estado de la denuncia fue actualizada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo actualizar el estado de la denuncia.");
        }
    }
    
    @PostMapping("/mandarCorreo")
    public ResponseEntity<?> mandarCorreo(Long denunciaId, String observacion) throws Exception {
        try {
            denunciaService.notificarEstadoSinCambio(denunciaId, observacion);
            return ResponseEntity.ok("Correo enviado correctamente");
        } catch (Exception e) {
            throw new Exception("Hubo un problema al mandar el correo: " + e.getMessage());
        }
    }
}
