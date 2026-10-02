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

    /**
     * Edita la información básica de un trabajador (RF-57).
     */
    void editarPerfilTrabajador(Long usuarioId, com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO request);

    /**
     * Edita la información básica de un contratante (RF-57).
     */
    void editarPerfilContratante(Long usuarioId, com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO request);

    /**
     * Da de baja lógicamente la cuenta de un usuario (RF-58).
     */
    void eliminarCuenta(Long usuarioId);
}

