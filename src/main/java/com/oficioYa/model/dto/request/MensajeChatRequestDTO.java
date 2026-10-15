package com.oficioya.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MensajeChatRequestDTO {
    
    @NotNull(message = "El remitente es requerido")
    private Long remitenteId;
    
    @NotNull(message = "El destinatario es requerido")
    private Long destinatarioId;
    
    @NotBlank(message = "El contenido no puede estar vacio")
    private String contenido;
}
