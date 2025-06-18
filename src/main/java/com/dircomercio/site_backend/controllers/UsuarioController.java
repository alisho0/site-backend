package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.UsuarioDTO;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/traerUsuarios")
    public List<UsuarioDTO> traerUsuarios() {

        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO usu = UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombreUsuario(usuario.getNombre())
                .build();
            usuariosDTO.add(usu);
        }
        return usuariosDTO;
    }
    
}
