package com.oficioya.service.impl;

import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.repository.OficioRepository;
import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.MetodoPago;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.repository.PortafolioMongoRepository;
import com.oficioya.persistence.document.PortafolioMongoDocument;
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
    private final OficioRepository oficioRepository;
    private final UsuarioRepository usuarioRepository;
    private final IPerfilTrabajadorValidator validator;
    private final PerfilTrabajadorEntityMapper entityMapper;
    private final PortafolioMongoRepository portafolioMongoRepository;

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

    @Override
    @Transactional
    public PerfilTrabajador registrarOficioPrincipal(Long perfilId, Long oficioId) {
        log.info("Registrando oficio principal {} para el perfil {}", oficioId, perfilId);
        
        PerfilTrabajadorEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de trabajador no encontrado con ID: " + perfilId));
                
        OficioEntity oficio = oficioRepository.findById(oficioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Oficio no encontrado con ID: " + oficioId));
                
        if (!oficio.isActivo()) {
            throw new EstadoInvalidoException("No se puede asignar un oficio inactivo como principal");
        }
        
        perfil.setOficioPrincipal(oficio);
        PerfilTrabajadorEntity actualizado = perfilRepository.save(perfil);
        
        return entityMapper.toDomain(actualizado);
    }


    @Override
    @Transactional
    public PerfilTrabajador registrarOficiosSecundarios(Long perfilId, List<Long> oficiosIds) {
        log.info("Registrando {} oficios secundarios para el perfil {}", oficiosIds.size(), perfilId);
        
        PerfilTrabajadorEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de trabajador no encontrado con ID: " + perfilId));
        
        List<OficioEntity> oficiosEncontrados = oficioRepository.findAllById(oficiosIds);
        
        if (oficiosEncontrados.size() != oficiosIds.size()) {
            throw new RecursoNoEncontradoException("Uno o más oficios secundarios proporcionados no existen en el catálogo");
        }
        
        for (OficioEntity oficio : oficiosEncontrados) {
            if (!oficio.isActivo()) {
                throw new EstadoInvalidoException("El oficio '" + oficio.getNombre() + "' se encuentra inactivo y no puede ser asignado");
            }
            if (perfil.getOficioPrincipal() != null && perfil.getOficioPrincipal().getId().equals(oficio.getId())) {
                throw new EstadoInvalidoException("El oficio '" + oficio.getNombre() + "' ya es el oficio principal y no puede ser secundario");
            }
        }
        
        perfil.setOficios(oficiosEncontrados);
        PerfilTrabajadorEntity actualizado = perfilRepository.save(perfil);
        
        return entityMapper.toDomain(actualizado);
    }


    @Override
    @Transactional
    public PerfilTrabajador actualizarDetallesEspecificos(Long perfilId, String detallesEspecificos) {
        log.info("Actualizando detalles específicos para el perfil {}", perfilId);
        
        PerfilTrabajadorEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de trabajador no encontrado con ID: " + perfilId));
        
        perfil.setDetallesEspecificos(detallesEspecificos);
        
        PerfilTrabajadorEntity actualizado = perfilRepository.save(perfil);
        
        return entityMapper.toDomain(actualizado);
    }


    @Override
    @Transactional
    public PerfilTrabajador actualizarPortafolio(Long perfilId, List<String> fotos) {
        log.info("Actualizando portafolio para el perfil {}. Cantidad de fotos recibidas: {}", perfilId, fotos != null ? fotos.size() : 0);
        
        PerfilTrabajadorEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de trabajador no encontrado con ID: " + perfilId));
        
        // 1. Validamos y guardamos en PostgreSQL (gestionado por @Transactional)
        PerfilTrabajadorEntity actualizado = perfilRepository.save(perfil);
        
        // 2. Transacción distribuida manual hacia MongoDB
        // Buscamos si ya existe un portafolio para este perfil
        PortafolioMongoDocument portafolioDocument = portafolioMongoRepository.findByPerfilTrabajadorId(perfilId)
                .orElse(PortafolioMongoDocument.builder().perfilTrabajadorId(perfilId).build());
        
        // Actualizamos los datos
        portafolioDocument.setFotosUrl(fotos);

        try {
            // Guardamos en Mongo
            portafolioMongoRepository.save(portafolioDocument);
        } catch (Exception e) {
            log.error("Error al guardar en MongoDB el portafolio del perfil {}: {}", perfilId, e.getMessage());
            // Si MongoDB falla, lanzamos una RuntimeException para que Spring @Transactional
            // intercepte y haga un ROLLBACK de los cambios hechos en PostgreSQL.
            throw new RuntimeException("Fallo al guardar en la base de datos de documentos. Revirtiendo transacción general.", e);
        }
        
        return entityMapper.toDomain(actualizado);
    }


    @Override
    @Transactional(readOnly = true)
    public PerfilTrabajador obtenerPerfilPorId(Long perfilId) {
        log.info("Consultando perfil de trabajador por ID: {}", perfilId);
        PerfilTrabajadorEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de trabajador no encontrado con ID: " + perfilId));
        PerfilTrabajador domain = entityMapper.toDomain(perfil);

        // Sincronizar fotos de portafolio desde MongoDB
        portafolioMongoRepository.findByPerfilTrabajadorId(perfilId)
                .ifPresent(doc -> {
                    if (doc.getFotosUrl() != null && !doc.getFotosUrl().isEmpty()) {
                        domain.setFotosPortafolio(doc.getFotosUrl());
                    }
                });

        return domain;
    }

    @Override
    public List<String> obtenerEspecializaciones(Long perfilId, Long oficioId) {
        log.info("Consultando especializaciones para perfil {} y oficio {}", perfilId, oficioId);
        return portafolioMongoRepository.findByPerfilTrabajadorId(perfilId)
                .map(doc -> {
                    var map = doc.getEspecializacionesPorOficioId();
                    if (map != null && map.containsKey(oficioId.toString())) {
                        return map.get(oficioId.toString());
                    }
                    return java.util.Collections.<String>emptyList();
                })
                .orElse(java.util.Collections.emptyList());
    }

    @Override
    public PerfilTrabajador actualizarEspecializaciones(Long perfilId, Long oficioId, List<String> especializaciones) {
        log.info("Actualizando especializaciones para perfil {} y oficio {}", perfilId, oficioId);
        
        PerfilTrabajadorEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil no encontrado"));

        com.oficioya.persistence.document.PortafolioMongoDocument portafolio = portafolioMongoRepository.findByPerfilTrabajadorId(perfilId)
                .orElse(com.oficioya.persistence.document.PortafolioMongoDocument.builder().perfilTrabajadorId(perfilId).build());

        if (portafolio.getEspecializacionesPorOficioId() == null) {
            portafolio.setEspecializacionesPorOficioId(new java.util.HashMap<>());
        }
        
        portafolio.getEspecializacionesPorOficioId().put(oficioId.toString(), especializaciones);
        portafolioMongoRepository.save(portafolio);
        
        return entityMapper.toDomain(perfil);
    }
}
