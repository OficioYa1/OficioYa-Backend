package com.oficioya.model.dto.response;

import lombok.Data;

@Data
public class PerfilTrabajadorResponseDTO {
    private Long id;
    private Long usuarioId;
    private String zonaCobertura;
    private Double calificacionPromedio;
    private Integer trabajosCompletados;
    private Boolean disponibleAhora;
}
