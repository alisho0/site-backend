package com.dircomercio.site_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExpedienteCreateMinimalDTO {
    private Long denunciaId;
    private String nro_exp; // preguntar a Ale, esto es para que se genere un numero de exp desde el inicio
}
