package com.oficioya.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DatosOperativosRequestDTO {
    
    @Schema(description = "Zonas de la ciudad donde atiende (ej. Norte, Centro, Chapinero)", example = "Norte, Chapinero")
    @NotBlank(message = "La zona de cobertura no puede estar en blanco")
    @Size(max = 255, message = "La zona de cobertura no puede exceder los 255 caracteres")
    private String zonaCobertura;

    @Schema(description = "Tarifa aproximada por hora de servicio", example = "50000.00")
    @NotNull(message = "La tarifa por hora no puede ser nula")
    @Positive(message = "La tarifa por hora debe ser mayor a cero")
    private BigDecimal tarifaPorHora;
}
