package com.oficioya.service.impl;

import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.model.domain.Usuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IUsuarioService;
import com.oficioya.validator.IUsuarioValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final IUsuarioValidator usuarioValidator;
    private final UsuarioEntityMapper entityMapper;

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        log.info("Registrando nuevo usuario: correo={}, rol={}", usuario.getCorreo(), usuario.getRol());

        usuarioValidator.validarCorreoUnico(usuario.getCorreo());

        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setCorreoVerificado(false);
        usuario.setTelefonoVerificado(false);
        usuario.setActivo(true);

        UsuarioEntity entity = entityMapper.toEntity(usuario);
        UsuarioEntity guardada = usuarioRepository.save(entity);

        log.info("Usuario registrado exitosamente: id={}, correo={}", guardada.getId(), guardada.getCorreo());

        return entityMapper.toDomain(guardada);
    }

    @Override
    @Transactional
    public void verificarCorreo(String correo) {
        log.info("Iniciando proceso de verificación de correo para: {}", correo);
        UsuarioEntity entity = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new com.oficioya.exception.UsuarioNoEncontradoException("No existe un usuario con el correo: " + correo));
        if (entity.isCorreoVerificado()) {
            log.warn("El correo {} ya había sido verificado anteriormente.", correo);
            throw new com.oficioya.exception.EstadoInvalidoException("El correo ya se encuentra verificado en el sistema.");
        }
        entity.setCorreoVerificado(true);
        usuarioRepository.save(entity);
        log.info("Correo {} verificado exitosamente.", correo);
    }
}