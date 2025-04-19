package com.dircomercio.site_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    

}
