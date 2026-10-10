package com.oficioya.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MensajeChatResponseDTO {
    private String id;
    private Long solicitudId;
    private Long remitenteId;
    private Long destinatarioId;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private boolean leido;
}
