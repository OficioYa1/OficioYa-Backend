package com.oficioYa.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Oficio {
    
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private boolean activo;
    
}
