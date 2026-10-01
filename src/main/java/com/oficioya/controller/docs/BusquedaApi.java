package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.FiltroBusquedaDTO;
import com.oficioya.model.dto.response.ErrorResponseDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Busqueda de Trabajadores", description = "Endpoints para realizar el matching y busqueda de profesionales")
public interface BusquedaApi {

    @Operation(summary = "Buscar trabajadores (RF-12 a RF-18, RF-32)",
            description = "Todos los filtros son opcionales y se combinan: categoría u oficio, zona, "
                    + "rango de tarifa, día y franja horaria, solo disponibles ahora y calificación mínima. "
                    + "Ordena por REPUTACION (por defecto) o DISTANCIA. Sin resultados devuelve una lista vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de trabajadores (puede estar vacía)"),
            @ApiResponse(responseCode = "400", description = "Parámetros con formato inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Combinación de filtros incoherente (tarifa o franja horaria)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<PerfilTrabajadorResponseDTO>> buscarTrabajadores(FiltroBusquedaDTO filtroBusquedaDTO);
}
