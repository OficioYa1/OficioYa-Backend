package com.oficioYa.service;

import com.oficioYa.model.domain.Solicitud;

public interface ISolicitudService {
    Solicitud crearSolicitud(Solicitud solicitud, Long contratanteId);
    void enviarSolicitud(Long solicitudId, Long trabajadorId);
    void aceptarSolicitud(Long solicitudId);
    void rechazarSolicitud(Long solicitudId);
    void iniciarSolicitud(Long solicitudId);
    void completarSolicitud(Long solicitudId);
    void cancelarSolicitud(Long solicitudId, String motivo);
}
