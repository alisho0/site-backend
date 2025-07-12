package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.CambiarPasswordDTO;
import com.dircomercio.site_backend.dtos.PerfilDTO;
import com.dircomercio.site_backend.dtos.UsuarioDTO;
import com.dircomercio.site_backend.implementation.UsuarioServiceImpl;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioServiceImpl usuarioServiceImpl;

    @GetMapping("/traerUsuarios")
    public ResponseEntity<?> traerUsuarios() {
        try {
            List<UsuarioDTO> usuarios = usuarioServiceImpl.traerUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error obteniendo los usuarios: "+ e.getMessage());
        }
    }
    
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) throws Exception {
        try {
            usuarioServiceImpl.borrarUsuario(id);
            return ResponseEntity.ok("Usuario borrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al borrar el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/perfilUsuario")
    public ResponseEntity<PerfilDTO> obtenerPerfil() {
        try {
            PerfilDTO perfil = usuarioServiceImpl.obtenerPerfil();
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/actualizarNombre")
    public ResponseEntity<String> actualizarNombre(@RequestBody PerfilDTO perfilDTO) {
        try {
            usuarioServiceImpl.actualizarPerfil(perfilDTO);
            return ResponseEntity.ok("Nombre actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el nombre: " + e.getMessage());
        }
    }

    @PutMapping("/cambiarPassword")
    public ResponseEntity<String> cambiarPassword(@RequestBody CambiarPasswordDTO dto) {
        try {
            usuarioServiceImpl.cambiarPassword(dto);
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cambiar la contraseña: " + e.getMessage());
        }
    }
}
