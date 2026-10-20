package com.oficioya.model.domain;

import com.oficioya.model.domain.state.*;
import com.oficioya.model.exception.EstadoInvalidoException;
import com.oficioya.persistence.entity.EstadoSolicitud;
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
        // Act
        // Assert
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
        // Arrange
        solicitud.setEstadoEnum(EstadoSolicitud.CREADA);
        
        // Assert (Transiciones inválidas)
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        // Act
        solicitud.enviar();

        // Assert
        assertEquals(EstadoSolicitud.ENVIADA, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("EnviadaState: permite aceptar, rechazar o cancelar")
    void testEnviadaState() {
        // Arrange
        solicitud.setEstadoEnum(EstadoSolicitud.ENVIADA);

        // Assert (Transiciones inválidas)
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());

        // Act
        // Assert (Rechazar)
        solicitud.rechazar();
        assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstadoEnum());

        // Act
        // Assert (Aceptar)
        solicitud.setEstadoEnum(EstadoSolicitud.ENVIADA);
        solicitud.aceptar();
        assertEquals(EstadoSolicitud.ACEPTADA, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("AceptadaState: permite iniciar o cancelar")
    void testAceptadaState() {
        // Arrange
        solicitud.setEstadoEnum(EstadoSolicitud.ACEPTADA);

        // Assert (Transiciones inválidas)
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());

        // Act
        solicitud.iniciar();

        // Assert
        assertEquals(EstadoSolicitud.EN_PROGRESO, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("EnProgresoState: permite completar y rechaza otras transiciones")
    void testEnProgresoState() {
        // Arrange
        solicitud.setEstadoEnum(EstadoSolicitud.EN_PROGRESO);

        // Assert (Transiciones inválidas)
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        // Act
        solicitud.completar();

        // Assert
        assertEquals(EstadoSolicitud.COMPLETADA, solicitud.getEstadoEnum());
    }

    @Test
    @DisplayName("CompletadaState, CanceladaState y RechazadaState son estados finales")
    void testEstadosFinales() {
        // Arrange & Assert (Completada)
        solicitud.setEstadoEnum(EstadoSolicitud.COMPLETADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.iniciar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.completar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        // Arrange & Assert (Cancelada)
        solicitud.setEstadoEnum(EstadoSolicitud.CANCELADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.cancelar());

        // Arrange & Assert (Rechazada)
        solicitud.setEstadoEnum(EstadoSolicitud.RECHAZADA);
        assertThrows(EstadoInvalidoException.class, () -> solicitud.enviar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.aceptar());
        assertThrows(EstadoInvalidoException.class, () -> solicitud.rechazar());
    }
}
