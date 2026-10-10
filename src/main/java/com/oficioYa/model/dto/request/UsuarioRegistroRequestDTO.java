package com.oficioYa.model.dto.request;

import com.oficioYa.persistence.entity.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRegistroRequestDTO(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo es inválido")
        String correo,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
        String telefono,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String contrasena,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @NotNull(message = "El rol del usuario es obligatorio (CONTRATANTE o TRABAJADOR)")
        RolUsuario rol
) {}

