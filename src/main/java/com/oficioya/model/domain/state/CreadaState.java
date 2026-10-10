package com.oficioya.model.domain.state;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.persistence.entity.EstadoSolicitud;

public class CreadaState extends AbstractSolicitudState {
    
    @Override
    public void enviar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.ENVIADA);
    }
}
