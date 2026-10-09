package com.oficioya.controller.docs;

import com.oficioya.model.dto.response.MensajeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Módulo Referidos", description = "Gestión de invitaciones e insignias de embajador")
public interface ReferidoApi {

    @Operation(summary = "RF-68 — Registrar referido", description = "Invita a un usuario por correo. Si el referido se registra luego, cuenta para la insignia del referente.")
    @PostMapping("/invitar")
    ResponseEntity<MensajeResponseDTO> registrarReferido(
            @RequestHeader("X-Usuario-Id") Long referenteId, 
            @RequestParam("correo") String correoReferido);
}
