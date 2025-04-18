package com.dircomercio.site_backend.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "denuncias")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private List<String> objeto;
    private List<String> motivo;
    private String estado;

    @ManyToMany
    @JoinTable(name = "denuncia_persona", // Nombre de la tabla intermedia 
        joinColumns = @JoinColumn(name = "denuncia_id"), // Hace referencia al id que será FK en la tabla intermedia de esta clase
        inverseJoinColumns = @JoinColumn(name = "persona_id") // Hace referencia al id que será FK en la tabla intermedia, pero de la otra clase
        )
    private List<Persona> personas;
    
    @OneToMany(mappedBy = "denuncia")
    private List<Documento> documentos;
}
