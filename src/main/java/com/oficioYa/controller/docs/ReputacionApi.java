package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.ReporteResenaRequestDTO;
import com.oficioya.model.dto.request.ResenaRequestDTO;
import com.oficioya.model.dto.response.ReputacionPorOficioDTO;
import com.oficioya.model.dto.response.ResenaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Módulo Reputación y Reseñas", description = "Endpoints para calificación mutua, reputación multioficio y moderación de reseñas")
public interface ReputacionApi {

    @Operation(summary = "Calificar a un trabajador por una solicitud finalizada",
            description = "Permite al contratante dejar una calificación (1-5 estrellas) y comentario tras un servicio completado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña registrada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de reseña inválidos o solicitud no finalizada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    ResponseEntity<ResenaResponseDTO> calificarTrabajador(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id,
            @Parameter(description = "ID del contratante") @RequestParam Long contratanteId,
            @Valid @RequestBody ResenaRequestDTO request);

    @Operation(summary = "Calificar a un contratante por una solicitud finalizada",
            description = "Permite al trabajador calificar al contratante tras un servicio completado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña registrada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de reseña inválidos"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    ResponseEntity<ResenaResponseDTO> calificarContratante(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id,
            @Parameter(description = "ID del trabajador") @RequestParam Long trabajadorId,
            @Valid @RequestBody ResenaRequestDTO request);

    @Operation(summary = "Reportar una reseña injusta o con contenido inapropiado",
            description = "Marca la reseña como reportada y notifica a los moderadores para revisión.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reporte procesado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    ResponseEntity<Void> reportarResena(
            @Parameter(description = "ID de la reseña") @PathVariable Long resenaId,
            @Valid @RequestBody ReporteResenaRequestDTO request);

    @Operation(summary = "Consultar reputación segmentada por oficio",
            description = "Retorna el promedio de estrellas y número total de calificaciones de un trabajador en un oficio específico (Reputación Multioficio).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reputación obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Trabajador u oficio no encontrado")
    })
    ResponseEntity<ReputacionPorOficioDTO> obtenerReputacionPorOficio(
            @Parameter(description = "ID del trabajador") @PathVariable Long trabajadorId,
            @Parameter(description = "ID del oficio") @PathVariable Long oficioId);

    @Operation(summary = "Consultar lista de reseñas recibidas por un usuario",
            description = "Devuelve el historial de opiniones y calificaciones públicas recibidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    ResponseEntity<List<ResenaResponseDTO>> obtenerResenasDeUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId);
}
