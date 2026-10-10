package com.oficioYa.model.domain;

import com.oficioYa.model.domain.state.*;
import com.oficioYa.model.exception.EstadoInvalidoException;
import com.oficioYa.persistence.entity.EstadoSolicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudStateTest {

    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        solicitud = new Solicitud();
    }

    @Test
    @DisplayName("SolicitudStateFactory retorna el estado correcto para cada enum")
    void testFactory() {
        assertTrue(SolicitudStateFactory.getState(null) instanceof CreadaState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.CREADA) instanceof CreadaState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.ENVIADA) instanceof EnviadaState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.ACEPTADA) instanceof AceptadaState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.RECHAZADA) instanceof RechazadaState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.EN_PROGRESO) instanceof EnProgresoState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.COMPLETADA) instanceof CompletadaState);
        assertTrue(SolicitudStateFactory.getState(EstadoSolicitud.CANCELADA) instanceof CanceladaState);
    }

    @Test
    @DisplayName("CreadaState: permite enviar y rechaza otras transiciones")
    void testCreadaState() {
        solicitud.setEstadoEnum(EstadoSolicitud.CREADA);
        
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        solicitud.enviar();
        assertEquals(EstadoSolicitud.ENVIADA, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("EnviadaState: permite aceptar, rechazar o cancelar")
    void testEnviadaState() {
        solicitud.setEstadoEnum(EstadoSolicitud.ENVIADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());

        solicitud.rechazar();
        assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstadoEnum());

        solicitud.setEstadoEnum(EstadoSolicitud.ENVIADA);
        solicitud.aceptar();
        assertEquals(EstadoSolicitud.ACEPTADA, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("AceptadaState: permite iniciar o cancelar")
    void testAceptadaState() {
        solicitud.setEstadoEnum(EstadoSolicitud.ACEPTADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());

        solicitud.iniciar();
        assertEquals(EstadoSolicitud.EN_PROGRESO, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("EnProgresoState: permite completar y rechaza otras transiciones")
    void testEnProgresoState() {
        solicitud.setEstadoEnum(EstadoSolicitud.EN_PROGRESO);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        solicitud.completar();
        assertEquals(EstadoSolicitud.COMPLETADA, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("CompletadaState, CanceladaState y RechazadaState son estados finales")
    void testEstadosFinales() {
        solicitud.setEstadoEnum(EstadoSolicitud.COMPLETADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        solicitud.setEstadoEnum(EstadoSolicitud.CANCELADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        solicitud.setEstadoEnum(EstadoSolicitud.RECHAZADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
    }
}
