package com.oficioYa.model.dto.response;

import com.oficioYa.persistence.entity.RolUsuario;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String correo,
        String telefono,
        String nombre,
        String apellido,
        RolUsuario rol,
        boolean correoVerificado,
        boolean telefonoVerificado,
        LocalDateTime fechaRegistro,
        boolean activo
) {}

