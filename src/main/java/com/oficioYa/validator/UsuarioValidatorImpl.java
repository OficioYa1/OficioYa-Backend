package com.oficioYa.validator;

import com.oficioYa.exception.CorreoYaRegistradoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.repository.UsuarioRepository;
import com.oficioYa.validator.impl.IUsuarioValidator;
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
