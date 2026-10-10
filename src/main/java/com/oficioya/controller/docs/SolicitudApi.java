package com.oficioYa.controller.docs;

import com.oficioYa.model.dto.request.SolicitudCreacionDTO;
import com.oficioYa.model.dto.response.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Solicitudes", description = "Endpoints para la gestion del ciclo de vida de las solicitudes de servicio")
public interface SolicitudApi {

    @Operation(summary = "Crear nueva solicitud", description = "Crea una solicitud en estado CREADA")
    ResponseEntity<SolicitudResponseDTO> crearSolicitud(@Valid SolicitudCreacionDTO solicitudCreacionDTO);

    @Operation(summary = "Enviar solicitud a un trabajador", description = "Transiciona la solicitud de CREADA a ENVIADA y notifica al trabajador")
    ResponseEntity<Void> enviarSolicitud(@PathVariable Long id, Long trabajadorId);

    @Operation(summary = "Aceptar solicitud", description = "Transiciona la solicitud de ENVIADA a ACEPTADA")
    ResponseEntity<Void> aceptarSolicitud(@PathVariable Long id);

    @Operation(summary = "Rechazar solicitud", description = "Transiciona la solicitud de ENVIADA a RECHAZADA")
    ResponseEntity<Void> rechazarSolicitud(@PathVariable Long id);

    @Operation(summary = "Iniciar solicitud", description = "Transiciona la solicitud a EN_PROGRESO")
    ResponseEntity<Void> iniciarSolicitud(@PathVariable Long id);

    @Operation(summary = "Completar solicitud", description = "Transiciona la solicitud a COMPLETADA")
    ResponseEntity<Void> completarSolicitud(@PathVariable Long id);

    @Operation(summary = "Cancelar solicitud", description = "Transiciona la solicitud a CANCELADA requiriendo un motivo")
    ResponseEntity<Void> cancelarSolicitud(@PathVariable Long id, String motivo);
}
