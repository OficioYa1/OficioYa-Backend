package com.oficioYa.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class FranjaDisponibilidadRequestDTO {

    @Schema(description = "Día de la semana", example = "MONDAY")
    @NotNull(message = "El día es obligatorio")
    private DayOfWeek dia;

    @Schema(description = "Hora de inicio (HH:mm)", example = "08:00", type = "string")
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @Schema(description = "Hora de fin (HH:mm)", example = "17:00", type = "string")
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
}
