package com.dircomercio.site_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/denuncia")
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    DocumentoService documentoService;

    @PostMapping("/test")
    public ResponseEntity<?> test(
    @RequestPart("denuncia") String denunciaJson,
    @RequestPart("file") MultipartFile file) throws JsonProcessingException {

    ObjectMapper mapper = new ObjectMapper();
    Denuncia denuncia = mapper.readValue(denunciaJson, Denuncia.class);

    System.out.println("Denuncia: " + denuncia);
    System.out.println("Archivo: " + file.getOriginalFilename());
    System.out.println("Personas: " + denuncia.getPersonas());

    return ResponseEntity.ok("Todo OK chango");
}


}
