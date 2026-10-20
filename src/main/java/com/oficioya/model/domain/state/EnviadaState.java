package com.oficioya.model.domain.state;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.persistence.entity.EstadoSolicitud;

public class EnviadaState extends AbstractSolicitudState {
    
    @Override
    public void aceptar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.ACEPTADA);
    }

    @Override
    public void rechazar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.RECHAZADA);
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.CANCELADA);
    }
}
