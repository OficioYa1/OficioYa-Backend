package com.oficioYa.model.domain.state;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.model.exception.EstadoInvalidoException;

public abstract class AbstractSolicitudState implements SolicitudState {
    
    protected String getNombreEstado() {
        return this.getClass().getSimpleName().replace("State", "").toUpperCase();
    }

    @Override
    public void enviar(Solicitud solicitud) {
        throw new EstadoInvalidoException("No se puede ENVIAR la solicitud desde el estado " + getNombreEstado());
    }

    @Override
    public void aceptar(Solicitud solicitud) {
        throw new EstadoInvalidoException("No se puede ACEPTAR la solicitud desde el estado " + getNombreEstado());
    }

    @Override
    public void rechazar(Solicitud solicitud) {
        throw new EstadoInvalidoException("No se puede RECHAZAR la solicitud desde el estado " + getNombreEstado());
    }

    @Override
    public void iniciar(Solicitud solicitud) {
        throw new EstadoInvalidoException("No se puede INICIAR la solicitud desde el estado " + getNombreEstado());
    }

    @Override
    public void completar(Solicitud solicitud) {
        throw new EstadoInvalidoException("No se puede COMPLETAR la solicitud desde el estado " + getNombreEstado());
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        throw new EstadoInvalidoException("No se puede CANCELAR la solicitud desde el estado " + getNombreEstado());
    }
}
