package com.oficioya.service.impl;

import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.MetodoPago;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IPerfilTrabajadorService;
import com.oficioya.validator.IPerfilTrabajadorValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerfilTrabajadorServiceImpl implements IPerfilTrabajadorService {

    private final PerfilTrabajadorRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final IPerfilTrabajadorValidator validator;
    private final PerfilTrabajadorEntityMapper entityMapper;

    @Override
    @Transactional
    public PerfilTrabajador crearPerfil(PerfilTrabajador perfil) {
        Long usuarioId = perfil.getUsuario().getId();
        log.info("Creando perfil de trabajador para usuarioId={}", usuarioId);

        validator.validarCuentaElegible(usuarioId);
        validator.validarPerfilNoExiste(usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró ningún usuario con el ID: " + usuarioId));

        perfil.setCalificacionPromedio(0.0);
        perfil.setTrabajosCompletados(0);
        perfil.setDisponibleAhora(false);

        PerfilTrabajadorEntity entity = entityMapper.toEntity(perfil);
        entity.setUsuario(usuario);
        PerfilTrabajadorEntity guardado = perfilRepository.save(entity);

        log.info("Perfil de trabajador creado: id={}, usuarioId={}", guardado.getId(), usuarioId);
        return entityMapper.toDomain(guardado);
    }

    @Override
    @Transactional
    public PerfilTrabajador actualizarZonaCobertura(Long perfilId, String zonaCobertura) {
        log.info("Actualizando zona de cobertura: perfilId={}, zona={}", perfilId, zonaCobertura);
        PerfilTrabajadorEntity entity = obtenerEntity(perfilId);
        entity.setZonaCobertura(zonaCobertura.trim());
        return guardar(entity);
    }

    @Override
    @Transactional
    public PerfilTrabajador actualizarTarifa(Long perfilId, BigDecimal tarifaPorHora) {
        log.info("Actualizando tarifa: perfilId={}, tarifa={}", perfilId, tarifaPorHora);
        PerfilTrabajadorEntity entity = obtenerEntity(perfilId);
        entity.setTarifaPorHora(tarifaPorHora);
        return guardar(entity);
    }

    @Override
    @Transactional
    public PerfilTrabajador actualizarDisponibilidadSemanal(Long perfilId, List<FranjaDisponibilidad> franjas) {
        log.info("Actualizando disponibilidad semanal: perfilId={}, franjas={}", perfilId, franjas.size());
        validator.validarFranjasDisponibilidad(franjas);
        PerfilTrabajadorEntity entity = obtenerEntity(perfilId);
        entity.getDisponibilidadSemanal().clear();
        entity.getDisponibilidadSemanal().addAll(entityMapper.toFranjasEntity(franjas));
        return guardar(entity);
    }

    @Override
    @Transactional
    public PerfilTrabajador actualizarMetodosPago(Long perfilId, Set<MetodoPago> metodosPago) {
        log.info("Actualizando métodos de pago: perfilId={}, metodos={}", perfilId, metodosPago);
        PerfilTrabajadorEntity entity = obtenerEntity(perfilId);
        entity.getMetodosPago().clear();
        entity.getMetodosPago().addAll(metodosPago);
        return guardar(entity);
    }

    @Override
    @Transactional
    public PerfilTrabajador activarDisponibleAhora(Long perfilId) {
        log.info("Activando 'Disponible ahora': perfilId={}", perfilId);
        PerfilTrabajadorEntity entity = obtenerEntity(perfilId);
        validator.validarPuedeActivarDisponibleAhora(entityMapper.toDomain(entity));
        entity.setDisponibleAhora(true);
        return guardar(entity);
    }

    @Override
    @Transactional
    public PerfilTrabajador desactivarDisponibleAhora(Long perfilId) {
        log.info("Desactivando 'Disponible ahora': perfilId={}", perfilId);
        PerfilTrabajadorEntity entity = obtenerEntity(perfilId);
        validator.validarPuedeDesactivarDisponibleAhora(entityMapper.toDomain(entity));
        entity.setDisponibleAhora(false);
        return guardar(entity);
    }

    private PerfilTrabajadorEntity obtenerEntity(Long perfilId) {
        return perfilRepository.findById(perfilId)
                .orElseThrow(() -> {
                    log.warn("Perfil de trabajador inexistente: id={}", perfilId);
                    return new RecursoNoEncontradoException("No se encontró el perfil de trabajador con ID: " + perfilId);
                });
    }

    private PerfilTrabajador guardar(PerfilTrabajadorEntity entity) {
        PerfilTrabajadorEntity guardado = perfilRepository.save(entity);
        log.info("Perfil de trabajador actualizado: id={}", guardado.getId());
        return entityMapper.toDomain(guardado);
    }
}
