package com.oficioya.model.domain.state;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.persistence.entity.EstadoSolicitud;

public class AceptadaState extends AbstractSolicitudState {
    
    @Override
    public void iniciar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.EN_PROGRESO);
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.CANCELADA);
    }
}
