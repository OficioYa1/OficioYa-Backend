package com.oficioya.listener;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.domain.event.SolicitudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener que implementa el rol de Observer en el sistema.
 * Escucha eventos de cambio de estado de solicitudes disparados desde SolicitudServiceImpl.
 */
@Slf4j
@Component
public class SolicitudEventListener {

    @EventListener
    public void onSolicitudCambioEstado(SolicitudEvent event) {
        if (event == null || event.getSolicitud() == null) {
            log.warn("Evento SolicitudEvent recibido con contenido nulo");
            return;
        }

        Solicitud solicitud = event.getSolicitud();
        log.info("OBSERVER: Evento de solicitud capturado. ID: {}, Nuevo Estado: {}",
                solicitud.getId(), solicitud.getEstadoEnum());
    }
}
