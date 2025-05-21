package com.dircomercio.site_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "denuncia_persona")
@Builder
public class DenunciaPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
	@JoinColumn(name = "denuncia_id")
    @JsonIgnore
	private Denuncia denuncia;

	@ManyToOne
	@JoinColumn(name = "persona_id")
    @JsonIgnore
	private Persona persona;

	private String rol;
    private String nombreDelegado;
    private String apellidoDelegado;
    private String dniDelegado;

}
