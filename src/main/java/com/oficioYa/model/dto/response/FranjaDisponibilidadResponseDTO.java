package com.oficioya.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class FranjaDisponibilidadResponseDTO {
    private DayOfWeek dia;
    @Schema(type = "string", example = "08:00")
    private LocalTime horaInicio;
    @Schema(type = "string", example = "17:00")
    private LocalTime horaFin;
}
