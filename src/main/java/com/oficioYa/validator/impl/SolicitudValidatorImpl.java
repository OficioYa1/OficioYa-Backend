package com.oficioya.validator.impl;

import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validator.ISolicitudValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudValidatorImpl implements ISolicitudValidator {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void validarContratante(Long contratanteId) {
        UsuarioEntity usuario = usuarioRepository.findById(contratanteId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "No se encontró ningún usuario con el ID: " + contratanteId));

        if (usuario.getRol() != RolUsuario.CONTRATANTE) {
            throw new EstadoInvalidoException("Solo un contratante puede crear solicitudes");
        }
        if (!usuario.isActivo()) {
            throw new EstadoInvalidoException("La cuenta del contratante se encuentra inactiva");
        }
    }
}
