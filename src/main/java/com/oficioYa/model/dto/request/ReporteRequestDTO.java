package com.oficioYa.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReporteRequestDTO(
    @NotNull(message = "El ID del usuario reportado es obligatorio")
    Long reportadoId,
    
    @NotBlank(message = "El motivo del reporte no puede estar vacío")
    @Size(max = 500, message = "El motivo no puede exceder los 500 caracteres")
    String motivo,
    
    String evidenciaUrl
) {}
