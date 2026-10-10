package com.oficioya.model.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudResponseDTO {
    private Long id;
    private Long contratanteId;
    private Long trabajadorId;
    private String descripcion;
    private String zonaServicio;
    private String fotoAdjuntaUrl;
    private String estado;
    private LocalDateTime fechaCreacion;
}
