package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;

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

    public PerfilDTO traerUsuarioPorId(Long id) throws Exception {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con ID: " + id));

        Persona persona = usuario.getPersona();

        return PerfilDTO.builder()
                .name(usuario.getNombre())
                .email(usuario.getEmail())
                .area(usuario.getRol() != null ? usuario.getRol().name() : null)
                .nombre(persona != null ? persona.getNombre() : null)
                .apellido(persona != null ? persona.getApellido() : null)
                .documento(persona != null ? persona.getDocumento() : null)
                .telefono(persona != null ? persona.getTelefono() : null)
                .domicilio(persona != null ? persona.getDomicilio() : null)
                .localidad(persona != null ? persona.getLocalidad() : null)
                .cp(persona != null ? persona.getCp() : null)
                .build();
    }

    public void actualizarUsuarioPorId(Long id, PerfilDTO perfilDTO) throws Exception {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        Persona persona = usuario.getPersona();
        if (persona == null) {
            throw new RuntimeException("La persona asociada al usuario no existe");
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

    public void borrarUsuario(Long id) throws Exception {
        Usuario usu = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        usuarioRepository.deleteById(usu.getId());
    }

    // --- MÉTODO CORREGIDO ---
    public void actualizarPerfil(PerfilDTO perfilDTO) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Persona persona = usuario.getPersona();
        if (persona == null) {
            throw new RuntimeException("La persona asociada al usuario no existe");
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

    // --- MÉTODO CORREGIDO ---
    public PerfilDTO obtenerPerfil() throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Persona persona = usuario.getPersona();

        return PerfilDTO.builder()
                .name(usuario.getNombre())
                .email(usuario.getEmail())
                .area(usuario.getRol() != null ? usuario.getRol().name() : null)
                .nombre(persona != null ? persona.getNombre() : null)
                .apellido(persona != null ? persona.getApellido() : null)
                .documento(persona != null ? persona.getDocumento() : null)
                .telefono(persona != null ? persona.getTelefono() : null)
                .domicilio(persona != null ? persona.getDomicilio() : null)
                .localidad(persona != null ? persona.getLocalidad() : null)
                .cp(persona != null ? persona.getCp() : null)
                .build();
    }

    // --- MÉTODO CORREGIDO ---
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
    
    private boolean validarPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}

