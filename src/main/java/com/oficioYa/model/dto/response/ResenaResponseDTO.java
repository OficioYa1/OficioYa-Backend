package com.oficioya.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResenaResponseDTO {
    private Long id;
    private Long autorId;
    private String autorNombre;
    private String autorFoto;
    private Long receptorId;
    private String receptorNombre;
    private Long oficioId;
    private String oficioNombre;
    private Integer estrellas;
    private String comentario;
    private LocalDateTime fechaCreacion;
}
