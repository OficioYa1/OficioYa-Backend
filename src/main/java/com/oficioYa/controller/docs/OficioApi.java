package com.oficioya.controller.docs;

import com.oficioya.model.dto.response.OficioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Catálogos", description = "API para gestionar oficios y catálogos")
public interface OficioApi {

    @Operation(summary = "RF-77 — Listar oficios base", description = "Retorna la lista de todos los oficios activos en el sistema para que puedan ser seleccionados en los perfiles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de oficios recuperada exitosamente")
    })
    ResponseEntity<List<OficioResponseDTO>> listarOficiosActivos();
}
