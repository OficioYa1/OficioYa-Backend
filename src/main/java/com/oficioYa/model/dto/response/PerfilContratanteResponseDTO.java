package com.oficioYa.model.dto.response;

import lombok.Data;

@Data
public class PerfilContratanteResponseDTO {
    private Long id;
    private Long usuarioId;
    private Double calificacionPromedio;
    private Integer serviciosSolicitados;
    private String descripcion;
}
