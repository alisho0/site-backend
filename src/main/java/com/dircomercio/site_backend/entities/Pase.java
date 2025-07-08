package com.dircomercio.site_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List; 


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pases")
@Data
public class Pase {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String asunto;
    private Long cantFolios;

    @Enumerated(EnumType.STRING)
    private Area areaOrigen;

    @Enumerated(EnumType.STRING)
    private Area areaDestino;
    private LocalDate fechaAccion;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "expediente_id", nullable = false)
    private Expediente expediente;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "pase")
    private List<Documento> documento;

}

