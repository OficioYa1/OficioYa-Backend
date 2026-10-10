package com.oficioYa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** RF-06: franja de disponibilidad guardada en la tabla perfil_disponibilidad. */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranjaDisponibilidadEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "dia", nullable = false)
    private DayOfWeek dia;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
}
