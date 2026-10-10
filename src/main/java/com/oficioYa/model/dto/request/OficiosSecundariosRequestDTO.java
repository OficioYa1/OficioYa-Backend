package com.oficioYa.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OficiosSecundariosRequestDTO {
    @Schema(description = "Lista de IDs de los oficios secundarios", example = "[2, 3]")
    @NotNull(message = "La lista de oficios no puede ser nula")
    @Size(max = 5, message = "No puedes registrar más de 5 oficios secundarios")
    private List<Long> oficiosIds;
}
