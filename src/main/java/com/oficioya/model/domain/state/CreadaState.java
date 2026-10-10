package com.oficioYa.model.domain.state;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.persistence.entity.EstadoSolicitud;

public class CreadaState extends AbstractSolicitudState {
    
    @Override
    public void enviar(Solicitud solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.ENVIADA);
    }
}
