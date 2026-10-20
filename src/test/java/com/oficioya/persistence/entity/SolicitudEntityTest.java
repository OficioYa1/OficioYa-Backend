package com.oficioya.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudEntityTest {

    @Test
    @DisplayName("prePersist - Si fechaCreacion es null la inicializa con fecha actual")
    void prePersist_FechaNula_Inicializa() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setFechaCreacion(null);
        assertNull(entity.getFechaCreacion());

        entity.prePersist();

        assertNotNull(entity.getFechaCreacion());
    }

    @Test
    @DisplayName("prePersist - Si fechaCreacion ya existe no la sobreescribe")
    void prePersist_FechaExistente_Conserva() {
        LocalDateTime anterior = LocalDateTime.of(2025, 1, 1, 10, 0);
        SolicitudEntity entity = SolicitudEntity.builder()
                .fechaCreacion(anterior)
                .build();

        entity.prePersist();

        assertEquals(anterior, entity.getFechaCreacion());
    }

    @Test
    @DisplayName("setEstado - Actualiza estado y fechaActualizacion")
    void setEstado_ActualizaEstadoYFecha() {
        SolicitudEntity entity = new SolicitudEntity();
        assertEquals(EstadoSolicitud.CREADA, entity.getEstado());
        assertNull(entity.getFechaActualizacion());

        entity.setEstado(EstadoSolicitud.EN_PROGRESO);

        assertEquals(EstadoSolicitud.EN_PROGRESO, entity.getEstado());
        assertNotNull(entity.getFechaActualizacion());
    }
}
