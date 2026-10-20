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

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private com.oficioya.service.notification.NotificacionStrategyFactory notificationFactory;

    public SolicitudEventListener() {
    }

    public SolicitudEventListener(com.oficioya.service.notification.NotificacionStrategyFactory notificationFactory) {
        this.notificationFactory = notificationFactory;
    }

    @EventListener
    public void onSolicitudCambioEstado(SolicitudEvent event) {
        if (event == null || event.getSolicitud() == null) {
            log.warn("Evento SolicitudEvent recibido con contenido nulo");
            return;
        }

        Solicitud solicitud = event.getSolicitud();
        log.info("OBSERVER: Evento de solicitud capturado. ID: {}, Nuevo Estado: {}",
                solicitud.getId(), solicitud.getEstadoEnum());

        if (notificationFactory != null) {
            try {
                com.oficioya.service.notification.INotificacionStrategy estrategia =
                        notificationFactory.getStrategy(com.oficioya.service.notification.CanalNotificacion.PUSH);
                estrategia.enviarNotificacion(
                        null,
                        "Actualización de Solicitud #" + solicitud.getId(),
                        "Tu solicitud de servicio ha cambiado de estado a: " + solicitud.getEstadoEnum()
                );
            } catch (Exception e) {
                log.warn("No fue posible despachar la notificación: {}", e.getMessage());
            }
        }
    }
}
