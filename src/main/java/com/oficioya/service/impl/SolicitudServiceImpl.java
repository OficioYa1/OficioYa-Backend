package com.oficioya.service.impl;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.domain.event.SolicitudEvent;
import com.oficioya.repository.SolicitudRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.ISolicitudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.mapper.SolicitudEntityMapper;
import com.oficioya.persistence.entity.SolicitudEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements ISolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SolicitudEntityMapper mapper;

    private Solicitud recuperarDominio(Long solicitudId) {
        SolicitudEntity entity = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no existe"));
        return mapper.toDomain(entity);
    }

    private void guardarDominio(Solicitud solicitud) {
        solicitudRepository.save(mapper.toEntity(solicitud));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Solicitud crearSolicitud(Solicitud solicitud, Long contratanteId) {
        log.info("Creando nueva solicitud para el contratante ID: {}", contratanteId);
        solicitud.setEstadoEnum(com.oficioya.persistence.entity.EstadoSolicitud.CREADA);
        // La entidad Usuario contratante deberia setearse en un caso real
        SolicitudEntity entity = mapper.toEntity(solicitud);
        entity = solicitudRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enviarSolicitud(Long solicitudId, Long trabajadorId) {
        log.info("Enviando solicitud {} al trabajador {}", solicitudId, trabajadorId);
        Solicitud solicitud = recuperarDominio(solicitudId);
        solicitud.enviar();
        guardarDominio(solicitud);
        eventPublisher.publishEvent(new SolicitudEvent(solicitud));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aceptarSolicitud(Long solicitudId) {
        log.info("Aceptando solicitud {}", solicitudId);
        Solicitud solicitud = recuperarDominio(solicitudId);
        solicitud.aceptar();
        guardarDominio(solicitud);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechazarSolicitud(Long solicitudId) {
        Solicitud solicitud = recuperarDominio(solicitudId);
        solicitud.rechazar();
        guardarDominio(solicitud);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void iniciarSolicitud(Long solicitudId) {
        Solicitud solicitud = recuperarDominio(solicitudId);
        solicitud.iniciar();
        guardarDominio(solicitud);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completarSolicitud(Long solicitudId) {
        Solicitud solicitud = recuperarDominio(solicitudId);
        solicitud.completar();
        guardarDominio(solicitud);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelarSolicitud(Long solicitudId, String motivo) {
        log.warn("Cancelando solicitud {} por motivo: {}", solicitudId, motivo);
        Solicitud solicitud = recuperarDominio(solicitudId);
        solicitud.cancelar();
        guardarDominio(solicitud);
    }
}
