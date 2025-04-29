package com.dircomercio.site_backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.entities.Denuncia;

public interface DenunciaService {

    void guardarDenuncia(Denuncia denuncia, List<MultipartFile> files);
}
