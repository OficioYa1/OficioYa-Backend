package com.oficioya.service;

import com.oficioya.model.domain.Usuario;

import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;

public interface IUsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    void verificarCorreo(String correo);
    PerfilTrabajadorResponseDTO obtenerPerfilTrabajador(Long usuarioId);
    /**
     * Obtiene el perfil público de un contratante por su ID de usuario (RF-53).
     * @param id Identificador único del usuario.
     * @return PerfilContratanteResponseDTO con la información del perfil.
     */
    PerfilContratanteResponseDTO obtenerPerfilContratante(Long id);
}

