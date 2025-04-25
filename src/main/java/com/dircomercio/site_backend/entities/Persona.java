package com.dircomercio.site_backend.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    private String tipoDocumento;
    private String domicilio;
    private String wpp;

    // @OneToOne(mappedBy = "persona")
    // private Usuario usuario;

    @ManyToMany(mappedBy = "personas") // Hace referencia a la lista de la otra clase
    private List<Denuncia> denuncias;

    @OneToMany(mappedBy = "persona")
    private List<Audiencia> audiencias;
}
