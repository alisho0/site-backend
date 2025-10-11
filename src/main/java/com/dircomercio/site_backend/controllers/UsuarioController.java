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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

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

    @GetMapping("/{id}")
    public ResponseEntity<?> traerUsuarioPorId(@PathVariable Long id) {
        try {
            PerfilDTO perfil = usuarioServiceImpl.traerUsuarioPorId(id);
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    // --- ESTE ES EL MÉTODO QUE FALTABA ---
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody PerfilDTO perfilDTO) {
        try {
            // Pasamos el ID y los datos al servicio para que actualice al usuario correcto
            usuarioServiceImpl.actualizarUsuarioPorId(id, perfilDTO); 
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) {
        try {
            usuarioServiceImpl.borrarUsuario(id);
            return ResponseEntity.ok("Usuario borrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al borrar el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/perfilUsuario")
    public ResponseEntity<?> obtenerPerfil() {
        try {
            PerfilDTO perfil = usuarioServiceImpl.obtenerPerfil();
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Este método es para que el propio usuario actualice su perfil, lo dejamos por si se usa en "Ajustes"
    @PutMapping("/actualizarNombre")
    public ResponseEntity<String> actualizarNombre(@RequestBody PerfilDTO perfilDTO) {
        try {
            usuarioServiceImpl.actualizarPerfil(perfilDTO);
            return ResponseEntity.ok("Perfil actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el perfil: " + e.getMessage());
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