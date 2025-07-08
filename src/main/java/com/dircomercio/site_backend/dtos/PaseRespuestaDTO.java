package com.dircomercio.site_backend.dtos;

import java.time.LocalDate;

import com.dircomercio.site_backend.entities.Area;

import lombok.Data;

@Data
public class PaseRespuestaDTO {
    private Long id;
    
    private String asunto;
    private Long cantFolios;
    private Area areaOrigen;
    private Area areaDestino;
    private String descripcion;
    private LocalDate fechaAccion;

    private String nroExpediente;
    private String nombreUsuario;
}
