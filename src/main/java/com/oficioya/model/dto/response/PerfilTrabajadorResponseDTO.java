package com.oficioya.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PerfilTrabajadorResponseDTO {
    private Long id;
    private Long usuarioId;
    private String descripcion;
    private String zonaCobertura;
    private BigDecimal tarifaPorHora;
    private Double calificacionPromedio;
    private Integer trabajosCompletados;
    private Boolean disponibleAhora;
}
