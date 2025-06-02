package com.dircomercio.site_backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class DenunciaDTO {
    private String descripcion;
    private List<String> motivo;
    private List<String> objeto;
    private List<PersonaRolDTO> personas;
}
