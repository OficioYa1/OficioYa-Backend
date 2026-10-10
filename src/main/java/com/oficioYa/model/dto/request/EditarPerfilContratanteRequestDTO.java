package com.oficioya.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para editar la información básica del perfil de un contratante.
 */
public record EditarPerfilContratanteRequestDTO(
        @NotBlank(message = "El teléfono es obligatorio")
        @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
        String telefono,

        @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
        String descripcion
) {}
