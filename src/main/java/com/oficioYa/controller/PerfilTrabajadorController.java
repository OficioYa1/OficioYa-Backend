package com.oficioya.controller;

import com.oficioya.controller.docs.PerfilTrabajadorApi;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.DisponibilidadSemanalRequestDTO;
import com.oficioya.model.dto.request.MetodosPagoRequestDTO;
import com.oficioya.model.dto.request.PerfilTrabajadorCreacionRequestDTO;
import com.oficioya.model.dto.request.TarifaRequestDTO;
import com.oficioya.model.dto.request.ZonaCoberturaRequestDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.service.IPerfilTrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfiles-trabajador")
@RequiredArgsConstructor
public class PerfilTrabajadorController implements PerfilTrabajadorApi {

    private final IPerfilTrabajadorService perfilService;
    private final PerfilTrabajadorMapper mapper;

    @Override
    @PostMapping
    public ResponseEntity<PerfilTrabajadorResponseDTO> crearPerfil(
            @Valid @RequestBody PerfilTrabajadorCreacionRequestDTO request) {
        PerfilTrabajador dominio = mapper.toDomain(request);
        PerfilTrabajador creado = perfilService.crearPerfil(dominio);
        return new ResponseEntity<>(mapper.toResponse(creado), HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}/zona-cobertura")
    public ResponseEntity<PerfilTrabajadorResponseDTO> actualizarZonaCobertura(
            @PathVariable Long id, @Valid @RequestBody ZonaCoberturaRequestDTO request) {
        PerfilTrabajador actualizado = perfilService.actualizarZonaCobertura(id, request.getZonaCobertura());
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @Override
    @PutMapping("/{id}/tarifa")
    public ResponseEntity<PerfilTrabajadorResponseDTO> actualizarTarifa(
            @PathVariable Long id, @Valid @RequestBody TarifaRequestDTO request) {
        PerfilTrabajador actualizado = perfilService.actualizarTarifa(id, request.getTarifaPorHora());
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @Override
    @PutMapping("/{id}/disponibilidad-semanal")
    public ResponseEntity<PerfilTrabajadorResponseDTO> actualizarDisponibilidadSemanal(
            @PathVariable Long id, @Valid @RequestBody DisponibilidadSemanalRequestDTO request) {
        PerfilTrabajador actualizado = perfilService.actualizarDisponibilidadSemanal(
                id, mapper.toFranjasDomain(request.getFranjas()));
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @Override
    @PutMapping("/{id}/metodos-pago")
    public ResponseEntity<PerfilTrabajadorResponseDTO> actualizarMetodosPago(
            @PathVariable Long id, @Valid @RequestBody MetodosPagoRequestDTO request) {
        PerfilTrabajador actualizado = perfilService.actualizarMetodosPago(id, request.getMetodosPago());
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @Override
    @PatchMapping("/{id}/disponible-ahora/activar")
    public ResponseEntity<PerfilTrabajadorResponseDTO> activarDisponibleAhora(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(perfilService.activarDisponibleAhora(id)));
    }

    @Override
    @PatchMapping("/{id}/disponible-ahora/desactivar")
    public ResponseEntity<PerfilTrabajadorResponseDTO> desactivarDisponibleAhora(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(perfilService.desactivarDisponibleAhora(id)));
    }

    @Override
    @PutMapping("/{id}/oficio-principal")
    public ResponseEntity<PerfilTrabajadorResponseDTO> registrarOficioPrincipal(
            @PathVariable Long id,
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.oficioya.model.dto.request.OficioPrincipalRequestDTO request) {
        PerfilTrabajador actualizado = perfilService.registrarOficioPrincipal(id, request.getOficioId());
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

}
