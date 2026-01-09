package com.dircomercio.site_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity // esta clase es una tabla en la base de datos
@Table(name = "auditoria_logs") // nombre de la tabla en la base de datos
@Data      // esto ya saben que hace asi que no se  lo documento
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaLog {

    @Id    //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incrementable
    private Long id; // id unico de cada log

    @Column(nullable = false) // no puede ser nulo
    private String usuario; //mail del user

    @Column(nullable = false)
    private String accion; // accion realizada (login, create, update o delete)

    @Column(nullable = false, length = 1000) //maximo 1000 caracteres
    private String descripcion; // descripcion de la accion realizada (detalles: "creo el expediente con id 5")

    @Column(nullable = false)
    private String ipOrigen; // ip de quien realizo la accion

    @Column(nullable = false)
    private LocalDateTime fechaHora; // fecha y hora de la accion

    @Column(nullable = false)
    private String resultado; // exito o fallo/succes o failure

    private String entidadAfectada; // entidad afectada (Expediente, Persona, Pase, etc.)

    private Long idEntidad; // id del expediente/docpmento modifixado
}


