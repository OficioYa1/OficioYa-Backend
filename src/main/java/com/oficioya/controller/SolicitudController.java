package com.oficioYa.controller;

import com.oficioYa.controller.docs.SolicitudApi;
import com.oficioYa.mapper.SolicitudMapper;
import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.model.dto.request.SolicitudCreacionDTO;
import com.oficioYa.model.dto.response.SolicitudResponseDTO;
import com.oficioYa.service.ISolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController implements SolicitudApi {

    private final ISolicitudService solicitudService;
    private final SolicitudMapper mapper;

    @Override
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud(@RequestBody SolicitudCreacionDTO solicitudCreacionDTO) {
        Solicitud dominio = mapper.toDomain(solicitudCreacionDTO);
        Solicitud creada = solicitudService.crearSolicitud(dominio, solicitudCreacionDTO.getContratanteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creada));
    }

    @Override
    @PostMapping("/{id}/enviar")
    public ResponseEntity<Void> enviarSolicitud(@PathVariable Long id, @RequestParam Long trabajadorId) {
        solicitudService.enviarSolicitud(id, trabajadorId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}/aceptar")
    public ResponseEntity<Void> aceptarSolicitud(@PathVariable Long id) {
        solicitudService.aceptarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<Void> rechazarSolicitud(@PathVariable Long id) {
        solicitudService.rechazarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarSolicitud(@PathVariable Long id) {
        solicitudService.iniciarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> completarSolicitud(@PathVariable Long id) {
        solicitudService.completarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSolicitud(@PathVariable Long id, @RequestParam String motivo) {
        solicitudService.cancelarSolicitud(id, motivo);
        return ResponseEntity.ok().build();
    }
}
