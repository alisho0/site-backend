package com.dircomercio.site_backend.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AudienciaDTO {
    private Long id;
    private LocalDateTime fecha;
    private String hora;
    private String lugar;

    private String nroExp; 
    private List<String> nombresPersonas; // Solo los nombres de las personas
}
