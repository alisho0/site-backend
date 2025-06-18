package com.dircomercio.site_backend.entities;

import java.util.List;

import com.dircomercio.site_backend.auth.repository.Token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user" , fetch =  FetchType.LAZY)
    private List<Token> tokens;

    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id")
    private Persona persona;

    @ManyToMany(mappedBy = "usuarios")
    private List<Expediente> expedientes;

    // @OneToMany(mappedBy = "usuario")
    // private List<Expediente> expedientesAsignadosLegacy; // Si quieres mantener la relación antigua

    @OneToMany(mappedBy = "usuario")
    private List<Pase> pases;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Rol rol;
}
