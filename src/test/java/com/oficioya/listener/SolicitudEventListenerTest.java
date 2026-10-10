package com.oficioya.listener;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.domain.event.SolicitudEvent;
import com.oficioya.persistence.entity.EstadoSolicitud;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SolicitudEventListenerTest {

    private final SolicitudEventListener listener = new SolicitudEventListener();

    @Test
    @DisplayName("onSolicitudCambioEstado - Evento valido no lanza excepcion y procesa")
    void onSolicitudCambioEstado_eventoValido_procesaExitosamente() {
        Solicitud solicitud = Solicitud.builder()
                .id(1L)
                .estadoEnum(EstadoSolicitud.CREADA)
                .build();
        SolicitudEvent event = new SolicitudEvent(solicitud);

        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(event));
    }

    @Test
    @DisplayName("onSolicitudCambioEstado - Evento nulo maneja defensivamente")
    void onSolicitudCambioEstado_eventoNulo_noLanzaExcepcion() {
        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(null));
        assertDoesNotThrow(() -> listener.onSolicitudCambioEstado(new SolicitudEvent(null)));
    }
}
