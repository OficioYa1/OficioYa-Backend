package com.oficioYa.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/** RF-06: reemplaza por completo la disponibilidad semanal. Una lista vacía la limpia. */
@Data
public class DisponibilidadSemanalRequestDTO {

    @NotNull(message = "La lista de franjas es obligatoria")
    @Size(max = 28, message = "No puede haber más de 28 franjas")
    @Valid
    private List<FranjaDisponibilidadRequestDTO> franjas;
}
