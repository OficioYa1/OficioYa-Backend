package com.oficioya.service;

import com.oficioya.model.domain.Usuario;

import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;

public interface IUsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    void verificarCorreo(String correo);
    PerfilTrabajadorResponseDTO obtenerPerfilTrabajador(Long usuarioId);
}

