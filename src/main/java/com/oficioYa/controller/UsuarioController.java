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
        PerfilTrabajadorResponseDTO response = usuarioService.obtenerPerfilTrabajador(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}/perfil-contratante")
    public ResponseEntity<PerfilContratanteResponseDTO> obtenerPerfilContratante(@PathVariable Long id) {
        PerfilContratanteResponseDTO perfil = usuarioService.obtenerPerfilContratante(id);
        return ResponseEntity.ok(perfil);
    }
}