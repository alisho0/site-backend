package com.dircomercio.site_backend.services;

import java.util.List;

import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Denuncia;

public interface DenunciaPersonaService {
    void vincularPersonaDenuncia(List<PersonaRolDTO> personas, Denuncia denuncia);
}
