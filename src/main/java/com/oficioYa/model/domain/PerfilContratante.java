package com.oficioya.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilContratante {
    private Long id;
    private Usuario usuario;
    private Double calificacionPromedio;
    private int serviciosSolicitados;
    private String descripcion;
}
