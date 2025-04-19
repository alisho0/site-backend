package com.dircomercio.site_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persitence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; 


public class Pase {

    @Id
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "pases")
    @Data

    private Long idPase;
    
    private String accion;
    
    private LocalDate fechaAccion;

    private String areaAccion;

    private String tipoTramite;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "expediente_id", nullable=false)
    private Expediente expediente;

}

