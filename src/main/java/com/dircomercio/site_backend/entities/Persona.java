package com.dircomercio.site_backend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    @Column(name = "codigo_postal")
    private String cp;
    private String localidad;
    private String documento;
    private String domicilio;

    // @OneToOne(mappedBy = "persona")
    // private Usuario usuario;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    private List<DenunciaPersona> denunciaPersonas;

    @ManyToMany(mappedBy = "personas")
    private List<Audiencia> audiencias;
}
