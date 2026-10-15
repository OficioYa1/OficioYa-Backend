package com.oficioya.controller;

import com.oficioya.controller.docs.ReferidoApi;
import com.oficioya.model.dto.response.MensajeResponseDTO;
import com.oficioya.service.IReferidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/referidos")
@RequiredArgsConstructor
public class ReferidoController implements ReferidoApi {

    private final IReferidoService referidoService;

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE')")
    @PostMapping("/invitar")
    public ResponseEntity<MensajeResponseDTO> registrarReferido(
            java.security.Principal principal, 
            @RequestParam("correo") String correoReferido) {
        referidoService.registrarReferido(principal.getName(), correoReferido);
        return ResponseEntity.ok(new MensajeResponseDTO("Invitación enviada exitosamente a " + correoReferido));
    }
}
