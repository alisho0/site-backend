package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.dtos.CambiarPasswordDTO;
import com.dircomercio.site_backend.dtos.PerfilDTO;
import com.dircomercio.site_backend.dtos.UsuarioDTO;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.PersonaRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonaRepository personaRepository;

    public List<UsuarioDTO> traerUsuarios() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO usu = UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombreUsuario(usuario.getNombre())
                .rol(usuario.getRol() != null ? usuario.getRol().name() : null)
                .build();
            usuariosDTO.add(usu);
        }
        return usuariosDTO;
    }

    public void borrarUsuario(Long id) throws Exception {
        Usuario usu = usuarioRepository.findById(id)
            .orElseThrow(() -> new Exception("Usuario no encontrado"));
        usuarioRepository.deleteById(usu.getId());
    }

    public void actualizarPerfil(PerfilDTO perfilDTO) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Persona persona = usuario.getPersona();
        if (persona == null) {
            throw new RuntimeException("La persona asociada al usuario no existe");
        }

        if (perfilDTO.getNombre() == null || perfilDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        usuario.setNombre(perfilDTO.getName());
        persona.setNombre(perfilDTO.getNombre());
        persona.setApellido(perfilDTO.getApellido());
        persona.setDocumento(perfilDTO.getDocumento());
        persona.setTelefono(perfilDTO.getTelefono());
        persona.setDomicilio(perfilDTO.getDomicilio());
        persona.setLocalidad(perfilDTO.getLocalidad());
        persona.setCp(perfilDTO.getCp());
        usuarioRepository.save(usuario);
        personaRepository.save(persona);
    }

    public PerfilDTO obtenerPerfil() throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return PerfilDTO.builder()
                .name(usuario.getNombre())
                .email(usuario.getEmail())
                .area(usuario.getRol() != null ? usuario.getRol().name() : null)
                .nombre(usuario.getPersona() != null ? usuario.getPersona().getNombre() : null)
                .apellido(usuario.getPersona() != null ? usuario.getPersona().getApellido() : null)
                .documento(usuario.getPersona() != null ? usuario.getPersona().getDocumento() : null)
                .telefono(usuario.getPersona() != null ? usuario.getPersona().getTelefono() : null)
                .domicilio(usuario.getPersona() != null ? usuario.getPersona().getDomicilio() : null)
                .localidad(usuario.getPersona() != null ? usuario.getPersona().getLocalidad() : null)
                .cp(usuario.getPersona() != null ? usuario.getPersona().getCp() : null)
                .build();
    }

    public void cambiarPassword(CambiarPasswordDTO dto) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getActual(), usuario.getContraseña())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        if (!dto.getNueva().equals(dto.getRepetir())) {
            throw new RuntimeException("Las contraseñas nuevas no coinciden");
        }

        if (!validarPassword(dto.getNueva())) {
            throw new RuntimeException("La nueva contraseña no cumple con los requisitos de seguridad");
        }

        usuario.setContraseña(passwordEncoder.encode(dto.getNueva()));
        usuarioRepository.save(usuario);
    }


    // Valida la contraseña según los requisitos
    private boolean validarPassword(String password) {
        // Requisitos: mínimo 8, una mayúscula, un número
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}
