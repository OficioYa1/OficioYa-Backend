package com.oficioYa.controller.docs;

import com.oficioYa.model.dto.request.ReporteRequestDTO;
import com.oficioYa.model.dto.response.MensajeResponseDTO;
import com.oficioYa.model.dto.response.ReporteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Tag(name = "Módulo Moderación", description = "Gestión de reportes, suspensiones y panel de administración")
public interface ModeracionApi {

    @Operation(summary = "RF-64 — Reportar usuario por comportamiento anómalo")
    ResponseEntity<ReporteResponseDTO> reportarUsuario(
        @RequestHeader("X-Usuario-Id") Long reportadorId, // Simulación temporal de auth
        @Valid @RequestBody ReporteRequestDTO request);

    @Operation(summary = "RF-73 — Consultar estado de un reporte enviado")
    ResponseEntity<ReporteResponseDTO> consultarEstadoReporte(@PathVariable Long id);

    @Operation(summary = "Panel Admin — Obtener todos los reportes")
    ResponseEntity<List<ReporteResponseDTO>> obtenerTodosLosReportes();

    @Operation(summary = "Panel Admin — Pausar cuenta de usuario")
    ResponseEntity<MensajeResponseDTO> pausarUsuario(@PathVariable Long id);
}
