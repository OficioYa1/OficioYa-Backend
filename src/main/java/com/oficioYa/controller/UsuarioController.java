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
    public ResponseEntity<java.util.Map<String, String>> verificarCorreo(@PathVariable String correo) {
        usuarioService.verificarCorreo(correo);
        return ResponseEntity.ok(java.util.Map.of("mensaje", "Correo verificado exitosamente"));
    }

}