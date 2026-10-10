package com.oficioYa.controller.docs;

import com.oficioYa.model.dto.request.MensajeChatRequestDTO;
import com.oficioYa.model.dto.response.MensajeChatResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mensajeria REST", description = "Operaciones de intercambio de mensajes entre usuarios")
public interface MensajeriaRestApi {

    @Operation(summary = "Enviar un mensaje en una solicitud")
    @PostMapping("/{solicitudId}/mensajes")
    ResponseEntity<MensajeChatResponseDTO> enviarMensaje(
            @PathVariable Long solicitudId,
            @Valid @RequestBody MensajeChatRequestDTO request);

    @Operation(summary = "Obtener los mensajes de una solicitud")
    @GetMapping("/{solicitudId}/mensajes")
    ResponseEntity<List<MensajeChatResponseDTO>> obtenerMensajesPorSolicitud(@PathVariable Long solicitudId);

    @Operation(summary = "Marcar mensajes como leidos")
    @PatchMapping("/{solicitudId}/mensajes/leidos")
    ResponseEntity<Void> marcarMensajesComoLeidos(
            @PathVariable Long solicitudId,
            @RequestParam Long destinatarioId);
}
