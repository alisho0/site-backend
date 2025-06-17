package com.dircomercio.site_backend.entities;

import java.util.List;

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
@Table(name = "usuarios")
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String contraseña;
    private String rol;
    private String area;

    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id")
    private Persona persona;

    @OneToMany(mappedBy = "usuario")
    private List<Expediente> expedientes;

    @OneToMany(mappedBy = "usuario")
    private List<Pase> pases;
}
