package com.oficioya.service;

import com.oficioya.exception.CorreoYaRegistradoException;
import com.oficioya.mapper.UsuarioDTOMapper;
import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.impl.UsuarioServiceImpl;
import com.oficioya.validator.IUsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private IUsuarioValidator usuarioValidator;

    @Mock
    private UsuarioDTOMapper dtoMapper;

    @Mock
    private UsuarioEntityMapper entityMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRegistroRequestDTO requestDTO;
    private Usuario domainUsuario;
    private UsuarioEntity usuarioEntity;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Preparamos datos dummy para cada prueba
        requestDTO = new UsuarioRegistroRequestDTO(
                "test@test.com", "1234567890", "password123", "Juan", "Perez", RolUsuario.CONTRATANTE
        );

        domainUsuario = Usuario.builder()
                .correo("test@test.com")
                .nombre("Juan")
                .apellido("Perez")
                .rol(RolUsuario.CONTRATANTE)
                .build();

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setCorreo("test@test.com");

        responseDTO = new UsuarioResponseDTO(
                1L, "test@test.com", "1234567890", "Juan", "Perez", RolUsuario.CONTRATANTE,
                false, false, null, true
        );
    }

    @Test
    void registrarUsuario_Exito() {
        doNothing().when(usuarioValidator).validarCorreoUnico(requestDTO.correo());
        when(dtoMapper.toDomain(requestDTO)).thenReturn(domainUsuario);
        when(entityMapper.toEntity(domainUsuario)).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(entityMapper.toDomain(usuarioEntity)).thenReturn(domainUsuario);
        when(dtoMapper.toResponseDTO(domainUsuario)).thenReturn(responseDTO);

        UsuarioResponseDTO result = usuarioService.registrarUsuario(requestDTO);

        assertNotNull(result);
        assertEquals("test@test.com", result.correo());
        assertEquals(RolUsuario.CONTRATANTE, result.rol());

        verify(usuarioValidator, times(1)).validarCorreoUnico(requestDTO.correo());
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void registrarUsuario_Falla_CorreoYaRegistrado() {
        doThrow(new com.oficioya.exception.CorreoYaRegistradoException("El correo ya se encuentra registrado."))
                .when(usuarioValidator).validarCorreoUnico(requestDTO.correo());

        CorreoYaRegistradoException exception = assertThrows(
                com.oficioya.exception.CorreoYaRegistradoException.class,
                () -> usuarioService.registrarUsuario(requestDTO)
        );

        assertEquals("El correo ya se encuentra registrado.", exception.getMessage());

        // Esta línea es clave para JaCoCo y SonarLint: asegura que si falla la validación, el flujo se corta
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
        verify(dtoMapper, never()).toDomain(any());
    }

}
