package com.oficioya.controller;

import com.oficioya.controller.docs.UsuarioApi;
import com.oficioya.mapper.UsuarioDTOMapper;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import com.oficioya.service.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.oficioya.model.dto.response.MensajeResponseDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioApi {

    private final IUsuarioService usuarioService;
    private final UsuarioDTOMapper dtoMapper;
    private final com.oficioya.mapper.PerfilTrabajadorMapper perfilTrabajadorMapper;
    private final com.oficioya.mapper.PerfilContratanteMapper perfilContratanteMapper;

    @Override
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroRequestDTO request) {

        Usuario dominio = dtoMapper.toDomain(request);
        Usuario registrado = usuarioService.registrarUsuario(dominio);
        UsuarioResponseDTO response = dtoMapper.toResponseDTO(registrado);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{correo}/verificar")
    public ResponseEntity<MensajeResponseDTO> verificarCorreo(@PathVariable String correo) {
        usuarioService.verificarCorreo(correo);
        return ResponseEntity.ok(new MensajeResponseDTO("Correo verificado exitosamente"));
    }

    @Override
    @GetMapping("/{id}/perfil-trabajador")
    public ResponseEntity<PerfilTrabajadorResponseDTO> obtenerPerfilTrabajador(@PathVariable Long id) {
        PerfilTrabajadorResponseDTO response = perfilTrabajadorMapper.toResponse(usuarioService.obtenerPerfilTrabajador(id));
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}/perfil-contratante")
    public ResponseEntity<PerfilContratanteResponseDTO> obtenerPerfilContratante(@PathVariable Long id) {
        PerfilContratanteResponseDTO perfil = perfilContratanteMapper.toResponse(usuarioService.obtenerPerfilContratante(id));
        return ResponseEntity.ok(perfil);
    }

    @Override
    @PutMapping("/{id}/perfil-trabajador")
    public ResponseEntity<MensajeResponseDTO> editarPerfilTrabajador(
            @PathVariable Long id, 
            @Valid @RequestBody com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO request) {
        
        usuarioService.editarPerfilTrabajador(id, request.telefono(), request.zonaCobertura());
        return ResponseEntity.ok(new MensajeResponseDTO("Perfil de trabajador actualizado exitosamente"));
    }

    @Override
    @PutMapping("/{id}/perfil-contratante")
    public ResponseEntity<MensajeResponseDTO> editarPerfilContratante(
            @PathVariable Long id, 
            @Valid @RequestBody com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO request) {
        
        usuarioService.editarPerfilContratante(id, request.telefono(), request.descripcion());
        return ResponseEntity.ok(new MensajeResponseDTO("Perfil de contratante actualizado exitosamente"));
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE')")
    public ResponseEntity<MensajeResponseDTO> eliminarCuenta(@PathVariable Long id) {
        usuarioService.eliminarCuenta(id);
        return ResponseEntity.ok(new MensajeResponseDTO("Cuenta eliminada exitosamente"));
    }

    @Override
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TRABAJADOR', 'CONTRATANTE')")
    @PostMapping(value = "/{id}/foto", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MensajeResponseDTO> actualizarFotoPerfil(
            @PathVariable Long id, 
            @org.springframework.web.bind.annotation.RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        usuarioService.actualizarFotoPerfil(id, file);
        return ResponseEntity.ok(new MensajeResponseDTO("Foto de perfil actualizada exitosamente"));
    }

    @Override
    public ResponseEntity<MensajeResponseDTO> recuperarCuenta(@RequestParam("correo") String correo) {
        usuarioService.recuperarCuenta(correo);
        return ResponseEntity.ok(new MensajeResponseDTO("Si el correo existe, recibirá un enlace de recuperación pronto."));
    }
}
