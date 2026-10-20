package com.oficioya.controller;

import com.oficioya.model.dto.request.ReporteResenaRequestDTO;
import com.oficioya.model.dto.request.ResenaRequestDTO;
import com.oficioya.model.dto.response.ReputacionPorOficioDTO;
import com.oficioya.model.dto.response.ResenaResponseDTO;
import com.oficioya.service.IReputacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oficioya.controller.docs.ReputacionApi;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reputacion")
@RequiredArgsConstructor
public class ReputacionController implements ReputacionApi {

    private final IReputacionService reputacionService;

    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('CONTRATANTE', 'ADMIN')")
    @PostMapping("/solicitud/{id}/trabajador")
    public ResponseEntity<ResenaResponseDTO> calificarTrabajador(
            @PathVariable Long id,
            @RequestParam Long contratanteId, // Simulación de sesión
            @Valid @RequestBody ResenaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reputacionService.calificarTrabajador(id, contratanteId, request));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TRABAJADOR', 'ADMIN')")
    @PostMapping("/solicitud/{id}/contratante")
    public ResponseEntity<ResenaResponseDTO> calificarContratante(
            @PathVariable Long id,
            @RequestParam Long trabajadorId, // Simulación de sesión
            @Valid @RequestBody ResenaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reputacionService.calificarContratante(id, trabajadorId, request));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('CONTRATANTE', 'TRABAJADOR', 'ADMIN')")
    @PostMapping("/resena/{resenaId}/reporte")
    public ResponseEntity<Void> reportarResena(
            @PathVariable Long resenaId,
            @Valid @RequestBody ReporteResenaRequestDTO request) {
        reputacionService.reportarResena(resenaId, request);
        return ResponseEntity.noContent().build();
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('CONTRATANTE', 'TRABAJADOR', 'ADMIN')")
    @GetMapping("/trabajador/{trabajadorId}/oficio/{oficioId}")
    public ResponseEntity<ReputacionPorOficioDTO> obtenerReputacionPorOficio(
            @PathVariable Long trabajadorId,
            @PathVariable Long oficioId) {
        return ResponseEntity.ok(reputacionService.obtenerReputacionPorOficio(trabajadorId, oficioId));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('CONTRATANTE', 'TRABAJADOR', 'ADMIN')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ResenaResponseDTO>> obtenerResenasDeUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reputacionService.obtenerResenasDeUsuario(usuarioId));
    }
}
