package com.dircomercio.site_backend.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AudienciaCreateDTO {
    private LocalDateTime fecha;
    private String hora;
    private String lugar;
    private Long expedienteId;
    private List<Long> personasIds;
}
