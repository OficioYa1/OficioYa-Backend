package com.oficioya.listener;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.domain.event.SolicitudEvent;
import com.oficioya.persistence.entity.EstadoSolicitud;
import com.oficioya.service.notification.CanalNotificacion;
import com.oficioya.service.notification.INotificacionStrategy;
import com.oficioya.service.notification.NotificacionStrategyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudEventListenerTest {

    @Mock
    private NotificacionStrategyFactory notificationFactory;

    @Mock
    private INotificacionStrategy notificacionStrategy;

    @Test
    @DisplayName("onSolicitudCambioEstado - Evento valido sin factory procesa sin error")
    void onSolicitudCambioEstado_sinFactory_procesaExitosamente() {
        SolicitudEventListener listener = new SolicitudEventListener();
        Solicitud solicitud = Solicitud.builder()
                .id(1L)
                .estadoEnum(EstadoSolicitud.CREADA)
                .build();
        SolicitudEvent event = new SolicitudEvent(solicitud);

        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(event));
    }

    @Test
    @DisplayName("onSolicitudCambioEstado - Con factory despacha notificacion push")
    void onSolicitudCambioEstado_conFactory_enviaNotificacion() {
        SolicitudEventListener listener = new SolicitudEventListener(notificationFactory);
        Solicitud solicitud = Solicitud.builder()
                .id(10L)
                .estadoEnum(EstadoSolicitud.ACEPTADA)
                .build();
        SolicitudEvent event = new SolicitudEvent(solicitud);

        when(notificationFactory.getStrategy(CanalNotificacion.PUSH)).thenReturn(notificacionStrategy);

        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(event));

        verify(notificationFactory).getStrategy(CanalNotificacion.PUSH);
        verify(notificacionStrategy).enviarNotificacion(isNull(), contains("#10"), contains("ACEPTADA"));
    }

    @Test
    @DisplayName("onSolicitudCambioEstado - Si despacho falla maneja excepcion sin propagar")
    void onSolicitudCambioEstado_errorAlDespachar_capturaExcepcion() {
        SolicitudEventListener listener = new SolicitudEventListener(notificationFactory);
        Solicitud solicitud = Solicitud.builder()
                .id(10L)
                .estadoEnum(EstadoSolicitud.COMPLETADA)
                .build();
        SolicitudEvent event = new SolicitudEvent(solicitud);

        when(notificationFactory.getStrategy(CanalNotificacion.PUSH))
                .thenThrow(new RuntimeException("Fallo en servicio push"));

        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(event));
    }

    @Test
    @DisplayName("onSolicitudCambioEstado - Evento nulo maneja defensivamente")
    void onSolicitudCambioEstado_eventoNulo_noLanzaExcepcion() {
        SolicitudEventListener listener = new SolicitudEventListener();
        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(null));
        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(new SolicitudEvent(null)));
    }
}
