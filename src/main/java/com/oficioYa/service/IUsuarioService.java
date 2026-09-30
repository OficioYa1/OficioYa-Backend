package com.oficioya.service;

import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;

public interface IUsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRegistroRequestDTO request);
}

