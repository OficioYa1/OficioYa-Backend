package com.oficioya.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FiltroBusquedaDTO {
    @NotNull(message = "La zona es obligatoria")
    private String zona;
    
    @NotNull(message = "El oficio es obligatorio")
    private Long oficioId;
    
    private Double calificacionMinima;
    private String orden; // DISTANCIA o REPUTACION
}
