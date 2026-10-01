package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Módulo Identidad — Usuarios", description = "Gestión de cuentas de usuario: registro, perfil y ciclo de vida")
public interface UsuarioApi {

    @Operation(summary = "Registrar nuevo usuario",
            description = "Crea una cuenta nueva de tipo CONTRATANTE o TRABAJADOR. Valida correo único y formato de campos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados (campo inválido o vacío)"),
            @ApiResponse(responseCode = "409", description = "Conflicto: el correo ya se encuentra registrado en la plataforma"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRegistroRequestDTO request);
}

