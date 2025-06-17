package com.dircomercio.site_backend.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.DocumentoRespuestaDTO;
import com.dircomercio.site_backend.entities.Documento;
import com.dircomercio.site_backend.services.DocumentoService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/doc")
public class DocumentoController {

    @Autowired
    DocumentoService documentoService;

    @GetMapping("/traerPorDenuncia/{id}")
    public ResponseEntity<?> obtenerPdfDenuncia(@PathVariable Long id) throws Exception {
        List<DocumentoRespuestaDTO> docs;
        try {
            docs = documentoService.traerDocumentosPorDenuncia(id);
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            throw new Exception("Problema en el controlador de pdf: " + e.getMessage());
        }
    }

    @GetMapping("/traerPorId/{id}")
    public ResponseEntity<byte[]> obtenerPdf(@PathVariable Long id) {
        try {
            Documento doc = documentoService.obtenerPorId(id);
            Path path = Paths.get(doc.getRuta());
            byte[] contenido = Files.readAllBytes(path);
            return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"" + doc.getNombre() + "\"")
                .body(contenido);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
