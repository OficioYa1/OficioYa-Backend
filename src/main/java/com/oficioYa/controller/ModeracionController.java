package com.oficioYa.controller;

import com.oficioYa.controller.docs.ModeracionApi;
import com.oficioYa.model.dto.request.ReporteRequestDTO;
import com.oficioYa.model.dto.response.MensajeResponseDTO;
import com.oficioYa.model.dto.response.ReporteResponseDTO;
import com.oficioYa.service.IModeracionService;
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

    // TODO: Cuando JWT (OFY-50) esté implementado, remover el header manual X-Usuario-Id 
    // y extraer el ID del usuario directamente desde el SecurityContext.
    // Además agregar @PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE')")
    @Override
    @PostMapping("/reportes")
    public ResponseEntity<ReporteResponseDTO> reportarUsuario(
            @RequestHeader("X-Usuario-Id") Long reportadorId, 
            @Valid @RequestBody ReporteRequestDTO request) {
        ReporteResponseDTO response = moderacionService.reportarUsuario(reportadorId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // TODO: Agregar @PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE', 'ADMIN')") cuando JWT esté listo
    @Override
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ReporteResponseDTO> consultarEstadoReporte(@PathVariable Long id) {
        return ResponseEntity.ok(moderacionService.consultarEstadoReporte(id));
    }

    // TODO: Agregar @PreAuthorize("hasRole('ADMIN')") cuando JWT esté listo
    @Override
    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteResponseDTO>> obtenerTodosLosReportes() {
        return ResponseEntity.ok(moderacionService.obtenerTodosLosReportes());
    }

    // TODO: Agregar @PreAuthorize("hasRole('ADMIN')") cuando JWT esté listo
    @Override
    @PatchMapping("/usuarios/{id}/pausar")
    public ResponseEntity<MensajeResponseDTO> pausarUsuario(@PathVariable Long id) {
        moderacionService.pausarUsuario(id);
        return ResponseEntity.ok(new MensajeResponseDTO("Usuario pausado exitosamente por moderación."));
    }
}
