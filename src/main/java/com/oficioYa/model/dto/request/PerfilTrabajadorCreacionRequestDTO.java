package com.oficioya.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * RF-01: datos para crear la ficha pública de un trabajador.
 * En el Sprint 02 no hay login, por eso el usuarioId viaja en el body.
 */
@Data
public class PerfilTrabajadorCreacionRequestDTO {

    @Schema(description = "ID del usuario (con rol TRABAJADOR) dueño del perfil", example = "1")
    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser un número positivo")
    private Long usuarioId;

    @Schema(description = "Presentación breve del trabajador", example = "Plomero con 10 años de experiencia en el norte de Bogotá")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;
}
