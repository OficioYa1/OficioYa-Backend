package com.oficioYa.validator;

import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validation.IUsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioValidatorImpl implements IUsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void validarExistencia(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No se encontró ningún usuario con el ID: " + usuarioId);
        }
    }

    @Override
    public void validarCorreoUnico(String correo) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ConflictoException("El correo " + correo + " ya se encuentra registrado.");
        }
    }
}
