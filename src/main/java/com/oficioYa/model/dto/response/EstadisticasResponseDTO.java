package com.oficioya.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstadisticasResponseDTO {
    private long total;
    private long completadas;
    private long canceladas;
    private long enProgreso;
    private long pendientes;
}
