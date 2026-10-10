package com.oficioYa.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReporteResenaRequestDTO {

    @NotBlank(message = "El motivo del reporte es obligatorio")
    private String motivo;

}
