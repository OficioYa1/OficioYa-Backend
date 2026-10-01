package com.oficioya.controller.docs;

import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Operation(summary = "RF-40 — Verificar correo de usuario",
               description = "Verifica el correo electrónico de una cuenta existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Correo verificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe usuario con ese correo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Estado inválido: el correo ya estaba verificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    ResponseEntity<java.util.Map<String, String>> verificarCorreo(@PathVariable String correo);

}