package com.oficioya.controller;

import com.oficioya.controller.docs.PerfilTrabajadorApi;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.PerfilTrabajadorCreacionRequestDTO;
import com.oficioya.model.dto.request.TarifaRequestDTO;
import com.oficioya.model.dto.request.ZonaCoberturaRequestDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.service.IPerfilTrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
