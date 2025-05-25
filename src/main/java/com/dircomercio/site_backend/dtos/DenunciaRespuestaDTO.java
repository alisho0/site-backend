package com.dircomercio.site_backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class DenunciaRespuestaDTO {
    private Long id;
    private String descripcion;
    private List<String> objeto;
    private List<String> motivo;
    private String estado;
    private List<PersonaConRolDTO> personas;
}
