package com.dircomercio.site_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/denuncia")
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    DocumentoService documentoService;

    @PostMapping("/subirDoc")
    public ResponseEntity<?> subirArchivos(@RequestParam("files") List<MultipartFile> files, @RequestBody Denuncia denuncia) {
        try {
            documentoService.guardarDocumentos(files, denuncia);
            System.out.println(denuncia.getDescripcion());
            System.out.println(denuncia.getPersonas().get(0).getNombre());
            return ResponseEntity.ok("Archivos subidos correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir los archivos: " + e.getMessage());
        }
    }
    
}
