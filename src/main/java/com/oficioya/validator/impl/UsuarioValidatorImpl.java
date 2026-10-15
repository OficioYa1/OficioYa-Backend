package com.oficioya.validator.impl;

import com.oficioya.exception.CorreoYaRegistradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validator.IUsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioValidatorImpl implements IUsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void validarExistencia(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException("No se encontró ningún usuario con el ID: " + usuarioId);
        }
    }

    @Override
    public void validarCorreoUnico(String correo) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new CorreoYaRegistradoException("El correo " + correo + " ya se encuentra registrado.");
        }
    }
}
