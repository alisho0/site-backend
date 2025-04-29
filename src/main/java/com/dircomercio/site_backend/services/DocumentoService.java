package com.dircomercio.site_backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Documento;

public interface DocumentoService {

    List<Documento> guardarDocumentos(List<MultipartFile> files, Denuncia denuncia) throws Exception;
}
