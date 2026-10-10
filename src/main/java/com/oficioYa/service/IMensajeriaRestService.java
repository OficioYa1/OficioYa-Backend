package com.oficioYa.service;

import com.oficioYa.model.dto.request.MensajeChatRequestDTO;
import com.oficioYa.model.dto.response.MensajeChatResponseDTO;

import java.util.List;

public interface IMensajeriaRestService {
    
    // RF-51
    MensajeChatResponseDTO enviarMensaje(Long solicitudId, MensajeChatRequestDTO request);
    
    List<MensajeChatResponseDTO> obtenerMensajesPorSolicitud(Long solicitudId);
    
    void marcarMensajesComoLeidos(Long solicitudId, Long destinatarioId);
}
