package com.oficioYa.model.domain.state;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.persistence.entity.EstadoSolicitud;

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
