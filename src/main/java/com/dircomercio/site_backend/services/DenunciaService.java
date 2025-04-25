package com.dircomercio.site_backend.services;

import java.util.List;
import java.util.Optional;

import com.dircomercio.site_backend.entities.Denuncia;

public interface DenunciaService {

    void guardarDenuncia(Denuncia denuncia);

    public Optional<Denuncia> buscarDenuncia(Long id);

    public List<Denuncia> listarDenuncias();

    public Denuncia actualizarEstado(Long id, String estado);

    public List<Denuncia> listarPorPersona(String tipoDocumento, String documento);
}
