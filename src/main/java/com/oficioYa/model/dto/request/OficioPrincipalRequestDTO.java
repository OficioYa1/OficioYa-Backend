package com.oficioYa.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OficioPrincipalRequestDTO {
    @Schema(description = "ID del oficio que será el principal", example = "1")
    @NotNull(message = "El ID del oficio es obligatorio")
    @Positive(message = "El ID del oficio debe ser un número positivo")
    private Long oficioId;
}
