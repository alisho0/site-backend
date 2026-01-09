// este dto es el que recibe datos publicos (cualquiera puede enviar)
package com.dircomercio.site_backend.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DenunciaDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 5000, message = "La descripción debe tener entre 10 y 5000 caracteres")
    private String descripcion;

    @NotNull(message = "El motivo no puede estar vacio")
    @NotEmpty(message = "Debe seleccionar al menos un motivo")
    private List<String> motivo;

    @NotNull(message = "El objeto no puede estar vacio")
    @NotEmpty(message = "Debe seleccionar al menos un objeto")
    private List<String> objeto;

    @NotNull(message = "Debe incluir al menos una persona")
    @NotEmpty(message = "Debe incluir al menos una persona")
    private List<PersonaRolDTO> personas;

    @NotNull(message = "El campo de notificación no puede estar vacío")
    private Boolean notificar;
}
