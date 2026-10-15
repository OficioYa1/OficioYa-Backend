package com.oficioya.model.domain.state;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.persistence.entity.EstadoSolicitud;

public class EnProgresoState extends AbstractSolicitudState {
    
    @Override
    public void completar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.COMPLETADA);
    }
}
