package com.oficioYa.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReputacionPorOficioDTO {
    private Long trabajadorId;
    private Long oficioId;
    private String oficioNombre;
    private Double calificacionPromedio;
    private Long totalResenas;
}
