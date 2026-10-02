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

import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.repository.PerfilContratanteRepository;
import com.oficioya.mapper.PerfilContratanteEntityMapper;
import com.oficioya.mapper.PerfilContratanteMapper;
import com.oficioya.persistence.entity.PerfilContratanteEntity;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;


@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilTrabajadorRepository perfilRepository;
    private final IUsuarioValidator usuarioValidator;
    private final UsuarioEntityMapper entityMapper;
    private final PerfilTrabajadorEntityMapper perfilEntityMapper;
    private final PerfilTrabajadorMapper perfilMapper;
    private final PerfilContratanteRepository perfilContratanteRepository;
    private final PerfilContratanteEntityMapper perfilContratanteEntityMapper;
    private final PerfilContratanteMapper perfilContratanteMapper;


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

        // Auto-crear el perfil si es contratante
        if (guardada.getRol() == RolUsuario.CONTRATANTE) {
            PerfilContratanteEntity perfilContratante = new PerfilContratanteEntity();
            perfilContratante.setUsuario(guardada);
            perfilContratanteRepository.save(perfilContratante);
            log.info("Perfil de contratante creado automáticamente para el usuario ID: {}", guardada.getId());
        }

        log.info("Usuario registrado exitosamente: id={}, correo={}", guardada.getId(), guardada.getCorreo());


        return entityMapper.toDomain(guardada);
    }

    @Override
    @Transactional
    public void verificarCorreo(String correo) {
        log.info("Iniciando proceso de verificación de correo para: {}", correo);
        UsuarioEntity entity = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No existe un usuario con el correo: " + correo));
        if (entity.isCorreoVerificado()) {
            log.warn("El correo {} ya había sido verificado anteriormente.", correo);
            throw new EstadoInvalidoException("El correo ya se encuentra verificado en el sistema.");
        }
        entity.setCorreoVerificado(true);
        usuarioRepository.save(entity);
        log.info("Correo {} verificado exitosamente.", correo);
    }

    @Override
    public PerfilTrabajadorResponseDTO obtenerPerfilTrabajador(Long usuarioId) {
        log.info("Consultando perfil de trabajador para el usuario ID: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });

        if (usuario.getRol() != RolUsuario.TRABAJADOR) {
            log.error("El usuario con ID {} no tiene rol de trabajador", usuarioId);
            throw new EstadoInvalidoException("El usuario consultado no es un trabajador");
        }

        PerfilTrabajadorEntity perfilEntity = perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.error("Perfil no encontrado para el trabajador con ID {}", usuarioId);
                    return new RecursoNoEncontradoException("Perfil de trabajador no encontrado");
                });

        return perfilMapper.toResponse(perfilEntityMapper.toDomain(perfilEntity));
    }


    @Override
    public PerfilContratanteResponseDTO obtenerPerfilContratante(Long usuarioId) {
        log.info("Consultando perfil de contratante para el usuario ID: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });

        if (usuario.getRol() != RolUsuario.CONTRATANTE) {
            log.error("El usuario con ID {} no tiene rol de contratante", usuarioId);
            throw new EstadoInvalidoException("El usuario consultado no es un contratante");
        }

        PerfilContratanteEntity perfilEntity = perfilContratanteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.error("Perfil no encontrado para el contratante con ID {}", usuarioId);
                    return new RecursoNoEncontradoException("Perfil de contratante no encontrado");
                });

        return perfilContratanteMapper.toResponse(perfilContratanteEntityMapper.toDomain(perfilEntity));
    }

    @Override
    @Transactional
    public void editarPerfilTrabajador(Long usuarioId, com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO request) {
        log.info("Editando perfil básico de trabajador para el usuario ID: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });

        if (usuario.getRol() != RolUsuario.TRABAJADOR) {
            log.error("El usuario con ID {} no tiene rol de trabajador", usuarioId);
            throw new EstadoInvalidoException("El usuario consultado no es un trabajador");
        }

        PerfilTrabajadorEntity perfilEntity = perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.error("Perfil no encontrado para el trabajador con ID {}", usuarioId);
                    return new RecursoNoEncontradoException("Perfil de trabajador no encontrado");
                });

        // Actualizamos Usuario (teléfono)
        usuario.setTelefono(request.telefono());
        usuarioRepository.save(usuario);

        // Actualizamos Perfil (zona de cobertura)
        perfilEntity.setZonaCobertura(request.zonaCobertura());
        perfilRepository.save(perfilEntity);

        log.info("Perfil de trabajador ID {} actualizado exitosamente", usuarioId);
    }

    @Override
    @Transactional
    public void editarPerfilContratante(Long usuarioId, com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO request) {
        log.info("Editando perfil básico de contratante para el usuario ID: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });

        if (usuario.getRol() != RolUsuario.CONTRATANTE) {
            log.error("El usuario con ID {} no tiene rol de contratante", usuarioId);
            throw new EstadoInvalidoException("El usuario consultado no es un contratante");
        }

        PerfilContratanteEntity perfilEntity = perfilContratanteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.error("Perfil no encontrado para el contratante con ID {}", usuarioId);
                    return new RecursoNoEncontradoException("Perfil de contratante no encontrado");
                });

        // Actualizamos Usuario (teléfono)
        usuario.setTelefono(request.telefono());
        usuarioRepository.save(usuario);

        // Actualizamos Perfil (descripción)
        perfilEntity.setDescripcion(request.descripcion());
        perfilContratanteRepository.save(perfilEntity);

        log.info("Perfil de contratante ID {} actualizado exitosamente", usuarioId);
    }
}
