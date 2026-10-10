package com.oficioYa.model.domain.state;

import com.oficioYa.persistence.entity.EstadoSolicitud;

public class SolicitudStateFactory {
    public static SolicitudState getState(EstadoSolicitud estado) {
        if (estado == null) return new CreadaState();
        switch (estado) {
            case CREADA: return new CreadaState();
            case ENVIADA: return new EnviadaState();
            case ACEPTADA: return new AceptadaState();
            case RECHAZADA: return new RechazadaState();
            case EN_PROGRESO: return new EnProgresoState();
            case COMPLETADA: return new CompletadaState();
            case CANCELADA: return new CanceladaState();
            default: throw new IllegalArgumentException("Estado desconocido");
        }
    }
}
