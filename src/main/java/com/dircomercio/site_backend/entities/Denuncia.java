package com.dircomercio.site_backend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "denuncias")
@Builder
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @JsonProperty("objeto")
    private List<String> objeto;
    @JsonProperty("motivo")
    private List<String> motivo;
    @Builder.Default
    private String estado = "EN ESPERA";
    
    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL)
    private List<DenunciaPersona> denunciaPersonas;

    private Boolean notificar;

    @OneToOne
    @JoinColumn(name = "expediente_id", referencedColumnName = "id", nullable = true)
    private Expediente expediente;

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL)
    private List<DenunciaEstado> historialEstados;

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL)
    private List<Documento> documentos;
}
