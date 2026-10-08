package com.oficioya.service.impl;

import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.model.domain.Usuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IUsuarioService;
import com.oficioya.service.IStorageService;
import com.oficioya.validator.IUsuarioValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.domain.PerfilContratante;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.repository.PerfilContratanteRepository;
import com.oficioya.mapper.PerfilContratanteEntityMapper;
import com.oficioya.persistence.entity.PerfilContratanteEntity;


@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final IStorageService storageService;
    private final PerfilTrabajadorRepository perfilRepository;
    private final IUsuarioValidator usuarioValidator;
    private final UsuarioEntityMapper entityMapper;
    private final PerfilTrabajadorEntityMapper perfilEntityMapper;
    private final PerfilContratanteRepository perfilContratanteRepository;
    private final PerfilContratanteEntityMapper perfilContratanteEntityMapper;


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
    public PerfilTrabajador obtenerPerfilTrabajador(Long usuarioId) {
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

        return perfilEntityMapper.toDomain(perfilEntity);
    }


    @Override
    public PerfilContratante obtenerPerfilContratante(Long usuarioId) {
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

        return perfilContratanteEntityMapper.toDomain(perfilEntity);
    }

    @Override
    @Transactional
    public void editarPerfilTrabajador(Long usuarioId, String telefono, String zonaCobertura) {
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
        usuario.setTelefono(telefono);
        usuarioRepository.save(usuario);

        // Actualizamos Perfil (zona de cobertura)
        perfilEntity.setZonaCobertura(zonaCobertura);
        perfilRepository.save(perfilEntity);

        log.info("Perfil de trabajador ID {} actualizado exitosamente", usuarioId);
    }

    @Override
    @Transactional
    public void editarPerfilContratante(Long usuarioId, String telefono, String descripcion) {
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
        usuario.setTelefono(telefono);
        usuarioRepository.save(usuario);

        // Actualizamos Perfil (descripción)
        perfilEntity.setDescripcion(descripcion);
        perfilContratanteRepository.save(perfilEntity);

        log.info("Perfil de contratante ID {} actualizado exitosamente", usuarioId);
    }

    @Override
    @Transactional
    public void eliminarCuenta(Long usuarioId) {
        log.info("Iniciando proceso de eliminación de cuenta para el usuario ID: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado para eliminación", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });

        if (!usuario.isActivo()) {
            log.error("La cuenta del usuario con ID {} ya se encuentra inactiva", usuarioId);
            throw new EstadoInvalidoException("La cuenta ya se encuentra dada de baja");
        }

        // Borrado lógico (RF-58)
        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        log.info("Cuenta de usuario ID {} dada de baja exitosamente", usuarioId);
    }

    @Override
    @Transactional
    public void actualizarFotoPerfil(Long usuarioId, org.springframework.web.multipart.MultipartFile archivo) {
        log.info("Iniciando actualización de foto de perfil para usuario ID: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });

        if (!usuario.isActivo()) {
             log.error("El usuario con ID {} está inactivo", usuarioId);
             throw new EstadoInvalidoException("No se puede actualizar la foto de una cuenta inactiva");
        }

        // Delegar el guardado físico al servicio de Storage
        String fotoUrl = storageService.guardarImagen(archivo);

        // Actualizar la entidad con la URL generada
        usuario.setFotoPerfil(fotoUrl);
        usuarioRepository.save(usuario);

        log.info("Foto de perfil actualizada exitosamente para usuario ID: {}", usuarioId);
    }
}
