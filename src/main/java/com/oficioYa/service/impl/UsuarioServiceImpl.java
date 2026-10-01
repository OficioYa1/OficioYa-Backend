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
        // TODO Sprint 3: encriptar usuario.getContrasena() con BCrypt

        UsuarioEntity entity = entityMapper.toEntity(usuario);
        UsuarioEntity guardada = usuarioRepository.save(entity);

        log.info("Usuario registrado exitosamente: id={}, correo={}", guardada.getId(), guardada.getCorreo());

        return entityMapper.toDomain(guardada);
    }
}
