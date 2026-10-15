package com.oficioya.model.dto.response;

import java.time.LocalDateTime;

public record ReporteResponseDTO(
    Long id,
    Long reportadorId,
    Long reportadoId,
    String motivo,
    String evidenciaUrl,
    String estado,
    LocalDateTime fechaCreacion
) {}
