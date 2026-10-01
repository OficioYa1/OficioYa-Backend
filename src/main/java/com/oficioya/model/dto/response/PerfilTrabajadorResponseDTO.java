package com.oficioya.model.dto.response;

import com.oficioya.model.domain.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class PerfilTrabajadorResponseDTO {
    private Long id;
    private Long usuarioId;
    private String descripcion;
    private String zonaCobertura;
    private BigDecimal tarifaPorHora;
    private List<FranjaDisponibilidadResponseDTO> disponibilidadSemanal;
    private Set<MetodoPago> metodosPago;
    private Double calificacionPromedio;
    private Integer trabajosCompletados;
    private Boolean disponibleAhora;
}
