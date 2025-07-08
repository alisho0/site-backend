package com.dircomercio.site_backend.implementation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DocumentoRespuestaDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Documento;
import com.dircomercio.site_backend.entities.Pase;
import com.dircomercio.site_backend.repositories.DocumentoRepository;
import com.dircomercio.site_backend.services.DocumentoService;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    @Autowired
    DocumentoRepository documentoRepository;

    @Override
    public List<Documento> guardarDocumentos(List<MultipartFile> files, Denuncia denuncia, Pase pase) throws Exception {
        List<Documento> documentos = new ArrayList<>();
        try {
            
            for (MultipartFile file : files) {
                
                Documento documento = new Documento(); // Por cada file, creamos un documento
                byte[] bytes = file.getBytes();
                String fileName = UUID.randomUUID().toString(); // Hacemos un nombre aleaotorio para evitar repetición
                String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); // Saca la extensión
                
                long maxFileSize = 10 * 1024 * 1024; // 10 MB
                
                if (file.getSize() > maxFileSize) {
                    throw new RuntimeException("El archivo " + file.getOriginalFilename() + " excede el tamaño máximo permitido.");
                }
                if (fileExtension.equals(".exe") || fileExtension.equals(".bat")) {
                    throw new RuntimeException("El archivo " + file.getOriginalFilename() + " no es un formato permitido.");
                }
                
                String newFileName = fileName + fileExtension; // Nombre nuevo más la extensión
                File folder = new File("src/main/resources/archivos/");
                if (!folder.exists()) { // Si no existe la carpeta, la creamos
                    folder.mkdirs();
                }
                Path path = Paths.get("src/main/resources/archivos/" + newFileName);
                documento.setNombre(newFileName);
                documento.setFormato(fileExtension);
                documento.setRuta(path.toString()); // Guardamos la ruta del archivo
                documento.setDenuncia(denuncia);
                documento.setPase(pase);
                Files.write(path, bytes);
                documentos.add(documento);
            }
            return (List<Documento>) documentoRepository.saveAll(documentos);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        }

    @Override
    public List<DocumentoRespuestaDTO> traerDocumentosPorDenuncia(Long denunciaId) throws Exception {
        try {
            List<Documento> docs = documentoRepository.findAllByDenunciaId(denunciaId);
            List<DocumentoRespuestaDTO> dtos = new ArrayList<>();
            for (Documento doc : docs) {
                dtos.add(new DocumentoRespuestaDTO(doc.getId(), doc.getNombre(), doc.getFormato()));
            }
            return dtos;
        } catch (Exception e) {
            throw new Exception("Error al traer documentos por denuncia: " + e.getMessage());
        }
    }

    @Override
    public Documento obtenerPorId(Long id) throws Exception {
        try {
            return documentoRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el documento con id: " + id));
        } catch (Exception e) {
            throw new Exception("No se encontró el pdf" + e.getMessage());
        }
    }
    }
