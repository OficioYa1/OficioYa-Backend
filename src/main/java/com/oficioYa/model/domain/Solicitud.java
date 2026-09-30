package com.oficioYa.model.domain;

import com.oficioYa.persistence.entity.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {
    
    private Long id;
    private Usuario contratante;
    private Usuario trabajador;
    private String descripcion;
    private String zonaServicio;
    private String fotoAdjuntaUrl;
    private EstadoSolicitud estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
}
