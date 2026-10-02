package com.oficioya.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para editar la información básica del perfil de un trabajador.
 */
public record EditarPerfilTrabajadorRequestDTO(
        @NotBlank(message = "El teléfono es obligatorio")
        @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
        String telefono,

        @NotBlank(message = "La zona de cobertura es obligatoria")
        @Size(max = 255, message = "La zona de cobertura no puede exceder los 255 caracteres")
        String zonaCobertura
) {}
