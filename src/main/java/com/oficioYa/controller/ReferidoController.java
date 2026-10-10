package com.oficioYa.controller;

import com.oficioYa.controller.docs.ReferidoApi;
import com.oficioYa.model.dto.response.MensajeResponseDTO;
import com.oficioYa.service.IReferidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/referidos")
@RequiredArgsConstructor
public class ReferidoController implements ReferidoApi {

    private final IReferidoService referidoService;

    // TODO: Reemplazar X-Usuario-Id por el SecurityContext y agregar @PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE')")
    @Override
    @PostMapping("/invitar")
    public ResponseEntity<MensajeResponseDTO> registrarReferido(
            @RequestHeader("X-Usuario-Id") Long referenteId, 
            @RequestParam("correo") String correoReferido) {
        referidoService.registrarReferido(referenteId, correoReferido);
        return ResponseEntity.ok(new MensajeResponseDTO("Invitación enviada exitosamente a " + correoReferido));
    }
}
