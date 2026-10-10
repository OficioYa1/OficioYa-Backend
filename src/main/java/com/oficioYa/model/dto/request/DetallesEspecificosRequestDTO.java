package com.oficioYa.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DetallesEspecificosRequestDTO {
    
    @Schema(description = "Detalles específicos del oficio en formato libre", example = "Solo trabajo con pintura acrílica y herramientas propias.")
    @NotBlank(message = "Los detalles específicos no pueden estar en blanco")
    @Size(max = 1000, message = "Los detalles específicos no pueden exceder los 1000 caracteres")
    private String detallesEspecificos;
}
