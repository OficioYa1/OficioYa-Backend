package com.oficioya.model.domain.state;

import com.oficioya.model.domain.Solicitud;

public interface SolicitudState {
    void enviar(Solicitud solicitud);
    void aceptar(Solicitud solicitud);
    void rechazar(Solicitud solicitud);
    void iniciar(Solicitud solicitud);
    void completar(Solicitud solicitud);
    void cancelar(Solicitud solicitud);
}
