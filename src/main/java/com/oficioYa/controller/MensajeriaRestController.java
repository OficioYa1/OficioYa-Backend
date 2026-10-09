package com.oficioya.controller;

import com.oficioya.controller.docs.MensajeriaRestApi;
import com.oficioya.model.dto.request.MensajeChatRequestDTO;
import com.oficioya.model.dto.response.MensajeChatResponseDTO;
import com.oficioya.service.IMensajeriaRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class MensajeriaRestController implements MensajeriaRestApi {

    private final IMensajeriaRestService mensajeriaService;

    @Override
    public ResponseEntity<MensajeChatResponseDTO> enviarMensaje(Long solicitudId, MensajeChatRequestDTO request) {
        return ResponseEntity.ok(mensajeriaService.enviarMensaje(solicitudId, request));
    }

    @Override
    public ResponseEntity<List<MensajeChatResponseDTO>> obtenerMensajesPorSolicitud(Long solicitudId) {
        return ResponseEntity.ok(mensajeriaService.obtenerMensajesPorSolicitud(solicitudId));
    }

    @Override
    public ResponseEntity<Void> marcarMensajesComoLeidos(Long solicitudId, Long destinatarioId) {
        mensajeriaService.marcarMensajesComoLeidos(solicitudId, destinatarioId);
        return ResponseEntity.noContent().build();
    }
}
