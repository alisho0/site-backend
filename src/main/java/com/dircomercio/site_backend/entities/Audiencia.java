package com.dircomercio.site_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "audiencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audiencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    private String hora;
    private String lugar;
    private String persona;

    @ManyToOne
    @JoinColumn(name = "id_expediente")
    private Expediente expediente;

    @ManyToMany
    @JoinTable(name = "audiencia_persona",
    joinColumns = @JoinColumn(name = "audiencia_id"),
    inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<Persona> personas;
}
