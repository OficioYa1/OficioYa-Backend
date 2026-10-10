package com.oficioYa.model.domain.state;

import com.oficioYa.model.domain.Solicitud;

public interface SolicitudState {
    void enviar(Solicitud solicitud);
    void aceptar(Solicitud solicitud);
    void rechazar(Solicitud solicitud);
    void iniciar(Solicitud solicitud);
    void completar(Solicitud solicitud);
    void cancelar(Solicitud solicitud);
}
