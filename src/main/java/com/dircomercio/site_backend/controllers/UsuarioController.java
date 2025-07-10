package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.CambiarPasswordDTO;
import com.dircomercio.site_backend.dtos.PerfilDTO;
import com.dircomercio.site_backend.dtos.UsuarioDTO;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/traerUsuarios")
    public List<UsuarioDTO> traerUsuarios() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO usu = UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombreUsuario(usuario.getNombre())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .build();
            usuariosDTO.add(usu);
        }
        return usuariosDTO;
    }
    
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) throws Exception {
        Usuario usu = usuarioRepository.findById(id)
            .orElseThrow(() -> new Exception("Algo pasó"));
        usuarioRepository.deleteById(usu.getId());
        return ResponseEntity.ok("Usuario eliminado");
    }

    @GetMapping("/perfilUsuario")
    public ResponseEntity<PerfilDTO> obtenerPerfil(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PerfilDTO perfilDTO = PerfilDTO.builder()
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .area(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .build();

        return ResponseEntity.ok(perfilDTO);
    }

    @PutMapping("/actualizarNombre")
    public ResponseEntity<String> actualizarNombre(Authentication authentication, @RequestBody PerfilDTO perfilDTO) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(perfilDTO.getNombre());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Nombre actualizado correctamente");
    }

    @PutMapping("/cambiarPassword")
    public ResponseEntity<String> cambiarPassword(Authentication authentication, @RequestBody CambiarPasswordDTO dto) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getActual(), usuario.getContraseña())) {
            return ResponseEntity.badRequest().body("La contraseña actual no es correcta");
        }

        if (!dto.getNueva().equals(dto.getRepetir())) {
            return ResponseEntity.badRequest().body("Las contraseñas nuevas no coinciden");
        }

        if (!validarPassword(dto.getNueva())) {
            return ResponseEntity.badRequest().body("La nueva contraseña no cumple con los requisitos: mínimo 8 caracteres, una mayúscula y un número");
        }

        usuario.setContraseña(passwordEncoder.encode(dto.getNueva()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    private boolean validarPassword(String password) {
        // Requisitos: mínimo 8, una mayúscula, un número
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}
