package com.oficioYa.model.domain.state;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.persistence.entity.EstadoSolicitud;

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
