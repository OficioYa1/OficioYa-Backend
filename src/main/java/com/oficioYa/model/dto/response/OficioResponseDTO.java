package com.oficioYa.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OficioResponseDTO {
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private boolean activo;
}
