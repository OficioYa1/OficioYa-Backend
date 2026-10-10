package com.oficioYa.model.domain.state;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.persistence.entity.EstadoSolicitud;

public class EnProgresoState extends AbstractSolicitudState {
    
    @Override
    public void completar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.COMPLETADA);
    }
}
