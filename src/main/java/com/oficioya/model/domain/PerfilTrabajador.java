package com.oficioya.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilTrabajador {
    
    private Long id;
    private Usuario usuario;
    private BigDecimal tarifaPorHora;
    private String zonaCobertura;
    @Builder.Default
    private List<FranjaDisponibilidad> disponibilidadSemanal = new ArrayList<>();
    private boolean disponibleAhora;
    @Builder.Default
    private Set<MetodoPago> metodosPago = new HashSet<>();
    private String detallesEspecificos;
    private String descripcion;
    private Double calificacionPromedio;
    private String fotosPortafolio;
    private int trabajosCompletados;
    private Oficio oficioPrincipal;
    @Builder.Default
    private List<Oficio> oficios = new ArrayList<>();
    
}
