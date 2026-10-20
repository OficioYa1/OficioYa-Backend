package com.oficioya.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

/** Filtros de búsqueda de trabajadores. Todos son opcionales y se combinan con AND. */
@Data
public class FiltroBusquedaDTO {

    @Schema(description = "RF-11: necesidad en lenguaje natural", example = "alguien que arregle una gotera")
    @Size(max = 200, message = "El texto no puede superar los 200 caracteres")
    private String texto;

    @Schema(description = "RF-12: categoría del oficio", example = "Hogar")
    private String categoria;

    @Schema(description = "RF-12: ID del oficio", example = "1")
    private Long oficioId;

    @Schema(description = "RF-13: zona donde se requiere el servicio", example = "Chapinero")
    private String zona;

    @Schema(description = "RF-14: tarifa mínima por hora (COP)", example = "20000")
    @DecimalMin(value = "0", message = "La tarifa mínima no puede ser negativa")
    private BigDecimal tarifaMin;

    @Schema(description = "RF-14: tarifa máxima por hora (COP)", example = "60000")
    @DecimalMin(value = "0", message = "La tarifa máxima no puede ser negativa")
    private BigDecimal tarifaMax;

    @Schema(description = "RF-15: día de la semana solicitado", example = "MONDAY")
    private DayOfWeek dia;

    @Schema(description = "RF-15: hora de inicio solicitada (HH:mm)", example = "09:00", type = "string")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaDesde;

    @Schema(description = "RF-15: hora de fin solicitada (HH:mm)", example = "11:00", type = "string")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaHasta;

    @Schema(description = "RF-32: solo trabajadores con 'Disponible ahora' activo", example = "false")
    private boolean soloDisponiblesAhora;

    @Schema(description = "RF-16: calificación mínima (0 a 5)", example = "4.0")
    @DecimalMin(value = "0.0", message = "La calificación mínima no puede ser negativa")
    @DecimalMax(value = "5.0", message = "La calificación mínima no puede superar 5")
    private Double calificacionMinima;

    @Schema(description = "RF-17/18: orden de los resultados", example = "REPUTACION", allowableValues = {"DISTANCIA", "REPUTACION"})
    @Pattern(regexp = "(?i)DISTANCIA|REPUTACION", message = "El orden debe ser DISTANCIA o REPUTACION")
    private String orden;
}
