package com.dircomercio.site_backend.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expedientes")
@Builder
public class Expediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nro_exp;
    private String cant_folios;
    private LocalDate fecha_inicio;
    private LocalDate fecha_finalizacion;
    private String hipervulnerable;
    private String delegacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @OneToOne(mappedBy = "expediente")
    private Denuncia denuncia;

    @OneToMany(mappedBy = "expediente")
    private List<Audiencia> audiencia;

    @OneToMany(mappedBy = "expediente")
    private List<Pase> pases;

    @ManyToMany
    @JoinTable(
        name = "expediente_usuario",
        joinColumns = @JoinColumn(name = "expediente_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios;
}
