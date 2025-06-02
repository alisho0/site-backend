package com.dircomercio.site_backend.dtos;


import lombok.Data;

@Data
public class ExpedienteCreateDTO {
    private Long id;
    private String nroExp;
    private String cantFolios;
    private String fechaInicio;
    private String fechaFinalizacion;
    private String hipervulnerable;
    private String delegacion;
    private Long usuarioId;
}

    

