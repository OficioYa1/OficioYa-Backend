package com.oficioya.service.impl;

import com.oficioya.mapper.UsuarioDTOMapper;
import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IUsuarioService;
import com.oficioya.validator.IUsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final IUsuarioValidator usuarioValidator;
    private final UsuarioDTOMapper dtoMapper;
    private final UsuarioEntityMapper entityMapper;

    @Override
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroRequestDTO request) {

        usuarioValidator.validarCorreoUnico(request.correo());

        Usuario usuario = dtoMapper.toDomain(request);

        usuario.setFechaRegistro(LocalDateTime.now());

        UsuarioEntity entity = entityMapper.toEntity(usuario);
        UsuarioEntity entityGuardada = usuarioRepository.save(entity);

        Usuario usuarioGuardado = entityMapper.toDomain(entityGuardada);
        return dtoMapper.toResponseDTO(usuarioGuardado);
    }
}

