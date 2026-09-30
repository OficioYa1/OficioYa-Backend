package com.oficioYa.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilTrabajador {
    
    private Long id;
    private Usuario usuario;
    private BigDecimal tarifaPorHora;
    private String zonaCobertura;
    private String disponibilidadSemanal;
    private boolean disponibleAhora;
    private String metodosPago;
    private String detallesEspecificos;
    private String descripcion;
    private Double calificacionPromedio;
    private String fotosPortafolio;
    private int trabajosCompletados;
    @Builder.Default
    private List<Oficio> oficios = new ArrayList<>();
    
}
