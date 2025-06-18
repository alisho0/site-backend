package com.dircomercio.site_backend.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ExpedienteUpdateDTO {
    private String nroExp;
    private String cant_folios;
    private LocalDate fecha_inicio;
    private LocalDate fecha_finalizacion;
    private String hipervulnerable;
    private String delegacion;
    private List<Long> usuarios;
}
