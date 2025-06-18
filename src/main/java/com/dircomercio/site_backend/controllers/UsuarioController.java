package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

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
    public List<Usuario> traerUsuarios() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            System.out.println("name: "+usuario.getEmail());
            System.out.println("id: "+usuario.getId());
        }
        return (List<Usuario>) usuarioRepository.findAll();
    }
    
}
