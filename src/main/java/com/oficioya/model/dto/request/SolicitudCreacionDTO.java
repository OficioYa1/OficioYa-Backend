package com.oficioya.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitudCreacionDTO {
    @NotNull(message = "El ID del contratante es obligatorio")
    private Long contratanteId;
    
    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;
    
    @NotBlank(message = "La zona de servicio es obligatoria")
    private String zonaServicio;
    
    private String fotoAdjuntaUrl;
}
