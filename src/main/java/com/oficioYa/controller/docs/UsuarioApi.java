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
import com.oficioya.model.dto.response.MensajeResponseDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;
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
    ResponseEntity<MensajeResponseDTO> verificarCorreo(@PathVariable String correo);

    @Operation(summary = "RF-52 — Consultar perfil de trabajador", description = "Obtiene la información pública de un usuario con rol de trabajador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil del trabajador obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerfilTrabajadorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe usuario o perfil con el ID especificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Estado inválido: el usuario consultado no tiene rol de trabajador", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilTrabajadorResponseDTO> obtenerPerfilTrabajador(@PathVariable Long id);

    @Operation(summary = "Obtener perfil de contratante", description = "Obtiene la información pública del perfil de un usuario con rol CONTRATANTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerfilContratanteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o perfil no creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "El usuario no tiene el rol de contratante",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    ResponseEntity<PerfilContratanteResponseDTO> obtenerPerfilContratante(
            @PathVariable Long id);

    @Operation(summary = "RF-57 — Editar perfil de trabajador", description = "Modifica la información básica (teléfono y zona de cobertura) de un trabajador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensajeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "El usuario no tiene el rol de trabajador", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    ResponseEntity<MensajeResponseDTO> editarPerfilTrabajador(
            @PathVariable Long id, 
            @Valid @RequestBody com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO request);

    @Operation(summary = "RF-57 — Editar perfil de contratante", description = "Modifica la información básica (teléfono y descripción) de un contratante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensajeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "El usuario no tiene el rol de contratante", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    ResponseEntity<MensajeResponseDTO> editarPerfilContratante(
            @PathVariable Long id, 
            @Valid @RequestBody com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO request);

    @Operation(summary = "RF-58 — Eliminar cuenta propia", description = "Realiza el borrado lógico de una cuenta de usuario, pasándola a estado inactivo para conservar el historial en plataforma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta dada de baja exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensajeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "La cuenta ya se encuentra dada de baja", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    ResponseEntity<MensajeResponseDTO> eliminarCuenta(@PathVariable Long id);

    @Operation(summary = "RF-59 — Subir foto de perfil", description = "Sube y asocia una foto de perfil (JPG, PNG) al usuario especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto subida y actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensajeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El archivo enviado está vacío o no es una imagen válida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "El usuario se encuentra inactivo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    @org.springframework.web.bind.annotation.PostMapping(value = "/{id}/foto", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MensajeResponseDTO> actualizarFotoPerfil(
            @PathVariable Long id, 
            @org.springframework.web.bind.annotation.RequestParam("file") org.springframework.web.multipart.MultipartFile file);

    @Operation(summary = "RF-75 — Recuperar cuenta", description = "Inicia el flujo de recuperación de contraseña enviando un enlace de recuperación al correo registrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enlace de recuperación enviado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensajeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Correo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "La cuenta se encuentra inactiva", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.oficioya.model.dto.response.ErrorResponseDTO.class)))
    })
    @org.springframework.web.bind.annotation.PostMapping("/recuperar-cuenta")
    ResponseEntity<MensajeResponseDTO> recuperarCuenta(@org.springframework.web.bind.annotation.RequestParam("correo") String correo);
}
