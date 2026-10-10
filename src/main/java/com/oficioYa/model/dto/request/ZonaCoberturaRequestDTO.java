package com.oficioYa.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** RF-04 */
@Data
public class ZonaCoberturaRequestDTO {

    @Schema(description = "Barrios, localidades o zonas de atención", example = "Chapinero, Usaquén")
    @NotBlank(message = "La zona de cobertura es obligatoria")
    @Size(max = 255, message = "La zona de cobertura no puede superar los 255 caracteres")
    private String zonaCobertura;
}
