package com.oficioya.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/** RF-05 */
@Data
public class TarifaRequestDTO {

    @Schema(description = "Tarifa aproximada por hora en COP", example = "35000")
    @NotNull(message = "La tarifa es obligatoria")
    @DecimalMin(value = "0.01", message = "La tarifa debe ser mayor que cero")
    @Digits(integer = 8, fraction = 2, message = "La tarifa admite hasta 8 enteros y 2 decimales")
    private BigDecimal tarifaPorHora;
}
