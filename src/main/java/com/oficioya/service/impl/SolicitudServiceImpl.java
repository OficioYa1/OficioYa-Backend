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

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements ISolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Solicitud crearSolicitud(Solicitud solicitud, Long contratanteId) {
        log.info("Creando nueva solicitud para el contratante ID: {}", contratanteId);
        solicitud.setEstadoEnum(com.oficioya.persistence.entity.EstadoSolicitud.CREADA);
        return solicitud;
    }

    @Override
    @Transactional
    public void enviarSolicitud(Long solicitudId, Long trabajadorId) {
        log.info("Enviando solicitud {} al trabajador {}", solicitudId, trabajadorId);
        Solicitud solicitud = getSolicitudDummy(solicitudId);
        solicitud.enviar();
        eventPublisher.publishEvent(new SolicitudEvent(solicitud));
    }

    @Override
    @Transactional
    public void aceptarSolicitud(Long solicitudId) {
        log.info("Aceptando solicitud {}", solicitudId);
        Solicitud solicitud = getSolicitudDummy(solicitudId);
        solicitud.aceptar();
    }

    @Override
    @Transactional
    public void rechazarSolicitud(Long solicitudId) {
        Solicitud solicitud = getSolicitudDummy(solicitudId);
        solicitud.rechazar();
    }

    @Override
    @Transactional
    public void iniciarSolicitud(Long solicitudId) {
        Solicitud solicitud = getSolicitudDummy(solicitudId);
        solicitud.iniciar();
    }

    @Override
    @Transactional
    public void completarSolicitud(Long solicitudId) {
        Solicitud solicitud = getSolicitudDummy(solicitudId);
        solicitud.completar();
    }

    @Override
    @Transactional
    public void cancelarSolicitud(Long solicitudId, String motivo) {
        log.warn("Cancelando solicitud {} por motivo: {}", solicitudId, motivo);
        Solicitud solicitud = getSolicitudDummy(solicitudId);
        solicitud.cancelar();
    }
    
    private Solicitud getSolicitudDummy(Long id) {
        Solicitud s = new Solicitud();
        s.setId(id);
        s.setEstadoEnum(com.oficioya.persistence.entity.EstadoSolicitud.CREADA);
        return s;
    }
}
