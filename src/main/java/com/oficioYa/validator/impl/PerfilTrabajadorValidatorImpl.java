package com.oficioya.validator.impl;

import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validator.IPerfilTrabajadorValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PerfilTrabajadorValidatorImpl implements IPerfilTrabajadorValidator {

    private final UsuarioRepository usuarioRepository;
    private final PerfilTrabajadorRepository perfilRepository;

    @Override
    public void validarCuentaElegible(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.warn("Creación de perfil: usuario inexistente id={}", usuarioId);
                    return new UsuarioNoEncontradoException("No se encontró ningún usuario con el ID: " + usuarioId);
                });

        if (usuario.getRol() != RolUsuario.TRABAJADOR) {
            log.warn("Creación de perfil: el usuario id={} no es TRABAJADOR (rol={})", usuarioId, usuario.getRol());
            throw new ReglaDeNegocioException("Solo un usuario con rol TRABAJADOR puede tener perfil de trabajador");
        }
        if (!usuario.isActivo()) {
            log.warn("Creación de perfil: el usuario id={} está inactivo", usuarioId);
            throw new ReglaDeNegocioException("La cuenta del usuario está inactiva");
        }
        if (!usuario.isCorreoVerificado() && !usuario.isTelefonoVerificado()) {
            log.warn("Creación de perfil: el usuario id={} no ha verificado correo ni teléfono", usuarioId);
            throw new ReglaDeNegocioException("La cuenta debe tener el correo o el teléfono verificado para crear el perfil");
        }
    }

    @Override
    public void validarPerfilNoExiste(Long usuarioId) {
        if (perfilRepository.existsByUsuarioId(usuarioId)) {
            log.warn("Creación de perfil: el usuario id={} ya tiene un perfil", usuarioId);
            throw new ConflictoException("El usuario " + usuarioId + " ya tiene un perfil de trabajador");
        }
    }

    @Override
    public void validarPerfilExiste(Long perfilId) {
        if (!perfilRepository.existsById(perfilId)) {
            log.warn("Perfil de trabajador inexistente id={}", perfilId);
            throw new RecursoNoEncontradoException("No se encontró el perfil de trabajador con ID: " + perfilId);
        }
    }
}
