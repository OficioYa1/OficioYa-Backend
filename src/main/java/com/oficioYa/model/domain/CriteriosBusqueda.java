package com.oficioya.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

/** Criterios (todos opcionales y combinables) con los que un contratante busca trabajadores. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriteriosBusqueda {
    /** RF-12: categoría del oficio (Hogar, Educación...). */
    private String categoria;
    /** RF-12: oficio específico. */
    private Long oficioId;
    /** RF-13: zona donde se requiere el servicio. */
    private String zona;
    /** RF-14: rango de tarifa por hora. */
    private BigDecimal tarifaMin;
    private BigDecimal tarifaMax;
    /** RF-15: día y franja solicitados. */
    private DayOfWeek dia;
    private LocalTime horaDesde;
    private LocalTime horaHasta;
    /** RF-32: solo quienes tienen "Disponible ahora" activo. */
    private boolean soloDisponiblesAhora;
    /** RF-16 (Dev C): calificación mínima. */
    private Double calificacionMinima;
    /** RF-17 / RF-18 (Dev C): DISTANCIA o REPUTACION. */
    private String orden;
}
