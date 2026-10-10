package com.oficioYa.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** RF-06: franja horaria habitual de atención en un día de la semana. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranjaDisponibilidad {
    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
