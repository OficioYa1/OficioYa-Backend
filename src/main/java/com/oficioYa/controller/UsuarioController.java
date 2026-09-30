package com.oficioya.controller;

import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import com.oficioya.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Módulo Identidad", description = "Endpoints para la gestión de cuentas de usuario (Desarrollador A)")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @Operation(summary = "Registrar un nuevo usuario (RF-49)",
            description = "Crea una nueva cuenta de contratante o trabajador en la plataforma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
            @ApiResponse(responseCode = "409", description = "Conflicto: El correo ya se encuentra registrado")
    })
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroRequestDTO request) {

        UsuarioResponseDTO response = usuarioService.registrarUsuario(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
