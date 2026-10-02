package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.DetallesEspecificosRequestDTO;
import com.oficioya.model.dto.request.DisponibilidadSemanalRequestDTO;
import com.oficioya.model.dto.request.MetodosPagoRequestDTO;
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

    @Operation(summary = "Definir disponibilidad semanal (RF-06)",
            description = "Reemplaza la disponibilidad semanal. Cada franja tiene día, hora de inicio y hora de fin; no pueden solaparse en el mismo día.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Franjas con horas inválidas o solapadas",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> actualizarDisponibilidadSemanal(
            @Parameter(description = "ID del perfil de trabajador") Long id, DisponibilidadSemanalRequestDTO request);

    @Operation(summary = "Configurar métodos de pago aceptados (RF-60)", description = "Valores: EFECTIVO, NEQUI, DAVIPLATA. Reemplaza los anteriores.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métodos de pago actualizados"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> actualizarMetodosPago(
            @Parameter(description = "ID del perfil de trabajador") Long id, MetodosPagoRequestDTO request);

    @Operation(summary = "Activar 'Disponible ahora' (RF-30)",
            description = "Muestra al trabajador como candidato para solicitudes inmediatas. Requiere zona de cobertura definida.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponible ahora activado"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Ya estaba activo o falta la zona de cobertura",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> activarDisponibleAhora(
            @Parameter(description = "ID del perfil de trabajador") Long id);

    @Operation(summary = "Desactivar 'Disponible ahora' (RF-31)",
            description = "Retira al trabajador de la búsqueda inmediata.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponible ahora desactivado"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "No estaba activo",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> desactivarDisponibleAhora(
            @Parameter(description = "ID del perfil de trabajador") Long id);

    @Operation(summary = "Registrar oficio principal (RF-02)", description = "Asigna el oficio principal al perfil del trabajador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oficio principal registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Perfil u Oficio no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> registrarOficioPrincipal(
            @Parameter(description = "ID del perfil de trabajador", required = true) Long id,
            com.oficioya.model.dto.request.OficioPrincipalRequestDTO request);


    @Operation(summary = "Registrar oficios secundarios (RF-03)", description = "Sobreescribe la lista de oficios secundarios del perfil.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oficios secundarios registrados"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. más de 5 oficios o intentando registrar el principal)"),
            @ApiResponse(responseCode = "404", description = "Perfil o algún oficio no encontrado")
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> registrarOficiosSecundarios(
            @Parameter(description = "ID del perfil de trabajador", required = true) Long id,
            com.oficioya.model.dto.request.OficiosSecundariosRequestDTO request);


    @Operation(summary = "Actualizar detalles específicos del oficio (RF-09)", description = "Permite al trabajador añadir detalles libres sobre su trabajo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalles específicos actualizados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. texto demasiado largo o vacío)"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> actualizarDetallesEspecificos(
            @Parameter(description = "ID del perfil de trabajador", required = true) Long id,
            DetallesEspecificosRequestDTO request);

}
