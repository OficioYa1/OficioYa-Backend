package com.oficioya.controller;

import com.oficioya.controller.docs.ModeracionApi;
import com.oficioya.model.dto.request.ReporteRequestDTO;
import com.oficioya.model.dto.response.MensajeResponseDTO;
import com.oficioya.model.dto.response.ReporteResponseDTO;
import com.oficioya.service.IModeracionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moderacion")
@RequiredArgsConstructor
public class ModeracionController implements ModeracionApi {

    private final IModeracionService moderacionService;

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE')")
    @PostMapping("/reportes")
    public ResponseEntity<ReporteResponseDTO> reportarUsuario(
            java.security.Principal principal, 
            @Valid @RequestBody ReporteRequestDTO request) {
        ReporteResponseDTO response = moderacionService.reportarUsuario(principal.getName(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE', 'ADMIN')")
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ReporteResponseDTO> consultarEstadoReporte(@PathVariable Long id) {
        return ResponseEntity.ok(moderacionService.consultarEstadoReporte(id));
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerTodosLosReportes() {
        return ResponseEntity.ok(moderacionService.obtenerTodosLosReportes());
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/usuarios/{id}/pausar")
    public ResponseEntity<MensajeResponseDTO> pausarUsuario(@PathVariable Long id) {
        moderacionService.pausarUsuario(id);
        return ResponseEntity.ok(new MensajeResponseDTO("Usuario pausado exitosamente por moderación."));
    }
}
