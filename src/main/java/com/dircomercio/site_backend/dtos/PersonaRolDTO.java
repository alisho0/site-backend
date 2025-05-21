package com.dircomercio.site_backend.dtos;

import com.dircomercio.site_backend.entities.Persona;

import lombok.Data;

@Data
public class PersonaRolDTO {
    private Persona persona;
    private String rol;
    private String nombreDelegado;
    private String apellidoDelegado;
    private String dniDelegado;
}
