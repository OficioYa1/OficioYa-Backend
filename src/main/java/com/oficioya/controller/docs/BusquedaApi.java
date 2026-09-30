package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.FiltroBusquedaDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Busqueda de Trabajadores", description = "Endpoints para realizar el matching y busqueda de profesionales")
public interface BusquedaApi {

    @Operation(summary = "Buscar trabajadores", description = "Filtra trabajadores por zona, oficio, calificacion y los ordena por distancia o reputacion")
    ResponseEntity<List<PerfilTrabajadorResponseDTO>> buscarTrabajadores(@org.springdoc.core.annotations.ParameterObject @Valid FiltroBusquedaDTO filtroBusquedaDTO);
}
