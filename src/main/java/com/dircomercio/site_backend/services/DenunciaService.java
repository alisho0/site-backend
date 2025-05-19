package com.dircomercio.site_backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.entities.Denuncia;

public interface DenunciaService {

    void guardarDenuncia(DenunciaDTO denuncia, List<MultipartFile> files);
    List<Denuncia> traerDenuncias() throws Exception;
}
