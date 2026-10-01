package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.PerfilTrabajadorCreacionRequestDTO;
import com.oficioya.model.dto.request.TarifaRequestDTO;
import com.oficioya.model.dto.request.ZonaCoberturaRequestDTO;
import com.oficioya.model.dto.response.ErrorResponseDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Perfil del Trabajador", description = "Configuración operativa del perfil de un trabajador (zona, tarifa, disponibilidad, pagos)")
public interface PerfilTrabajadorApi {

    @Operation(summary = "Crear perfil de trabajador (RF-01)",
            description = "Crea la ficha pública de un usuario TRABAJADOR con la cuenta verificada. Sin login en el Sprint 02: el usuarioId viaja en el body.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Perfil creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "El usuario ya tiene perfil",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Usuario no es trabajador, está inactivo o no está verificado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> crearPerfil(PerfilTrabajadorCreacionRequestDTO request);

    @Operation(summary = "Definir zona de cobertura (RF-04)", description = "Reemplaza la zona de cobertura del perfil.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zona actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> actualizarZonaCobertura(
            @Parameter(description = "ID del perfil de trabajador") Long id, ZonaCoberturaRequestDTO request);

    @Operation(summary = "Registrar tarifa aproximada (RF-05)", description = "Tarifa por hora en COP. Debe ser mayor que cero.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> actualizarTarifa(
            @Parameter(description = "ID del perfil de trabajador") Long id, TarifaRequestDTO request);
}
