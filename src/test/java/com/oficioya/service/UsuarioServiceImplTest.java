package com.oficioya.service;

import com.oficioya.exception.CorreoYaRegistradoException;
import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.model.domain.Usuario;
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

import java.time.LocalDateTime;
import java.util.Optional;

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
    private UsuarioEntityMapper entityMapper;

    @Mock
    private com.oficioya.repository.PerfilTrabajadorRepository perfilRepository;

    @Mock
    private com.oficioya.mapper.PerfilTrabajadorEntityMapper perfilEntityMapper;

    @Mock
    private com.oficioya.mapper.PerfilTrabajadorMapper perfilMapper;


    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario domainUsuario;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        domainUsuario = Usuario.builder()
                .correo("juan@test.com")
                .telefono("3001234567")
                .contrasena("password123")
                .nombre("Juan")
                .apellido("Perez")
                .rol(RolUsuario.CONTRATANTE)
                .build();

        usuarioEntity = UsuarioEntity.builder()
                .id(1L)
                .correo("juan@test.com")
                .telefono("3001234567")
                .contrasena("password123")
                .nombre("Juan")
                .apellido("Perez")
                .rol(RolUsuario.CONTRATANTE)
                .correoVerificado(false)
                .telefonoVerificado(false)
                .fechaRegistro(LocalDateTime.now())
                .activo(true)
                .build();
    }

    @Test
    void registrarUsuario_success_returnsRegisteredUser() {
        doNothing().when(usuarioValidator).validarCorreoUnico(domainUsuario.getCorreo());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(entityMapper.toDomain(usuarioEntity)).thenReturn(domainUsuario);

        Usuario result = usuarioService.registrarUsuario(domainUsuario);

        assertNotNull(result);
        assertEquals("juan@test.com", result.getCorreo());
        assertEquals(RolUsuario.CONTRATANTE, result.getRol());
        verify(usuarioValidator, times(1)).validarCorreoUnico(domainUsuario.getCorreo());
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void registrarUsuario_whenDuplicateEmail_throwsCorreoYaRegistradoException() {
        doThrow(new CorreoYaRegistradoException("El correo juan@test.com ya se encuentra registrado."))
                .when(usuarioValidator).validarCorreoUnico(domainUsuario.getCorreo());

        CorreoYaRegistradoException ex = assertThrows(
                CorreoYaRegistradoException.class,
                () -> usuarioService.registrarUsuario(domainUsuario)
        );

        assertEquals("El correo juan@test.com ya se encuentra registrado.", ex.getMessage());
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
        verify(entityMapper, never()).toEntity(any(Usuario.class));
    }

    @Test
    void registrarUsuario_success_setsDefaultValues() {
        doNothing().when(usuarioValidator).validarCorreoUnico(any());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(entityMapper.toDomain(any(UsuarioEntity.class))).thenReturn(domainUsuario);

        usuarioService.registrarUsuario(domainUsuario);

        assertFalse(domainUsuario.isCorreoVerificado(), "El correo NO debe estar verificado al registrarse");
        assertFalse(domainUsuario.isTelefonoVerificado(), "El teléfono NO debe estar verificado al registrarse");
        assertTrue(domainUsuario.isActivo(), "El usuario debe estar activo al registrarse");
        assertNotNull(domainUsuario.getFechaRegistro(), "La fecha de registro debe establecerse");
    }

    @Test
    void registrarUsuario_whenRepositoryFails_propagatesException() {
        doNothing().when(usuarioValidator).validarCorreoUnico(any());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class)))
                .thenThrow(new RuntimeException("Error de conexión con la base de datos"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.registrarUsuario(domainUsuario)
        );

        assertTrue(ex.getMessage().contains("Error de conexión"));
    }

    @Test
    void registrarUsuario_success_invokesEntityMapperCorrectly() {
        doNothing().when(usuarioValidator).validarCorreoUnico(any());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(entityMapper.toDomain(usuarioEntity)).thenReturn(domainUsuario);

        usuarioService.registrarUsuario(domainUsuario);

        verify(entityMapper, times(1)).toEntity(any(Usuario.class));
        verify(entityMapper, times(1)).toDomain(usuarioEntity);
    }

    @Test
    void verificarCorreo_UsuarioExisteYNoEstaVerificado_VerificaYGuarda() {
        // Arrange
        String correo = "test@test.com";
        UsuarioEntity entity = new UsuarioEntity();
        entity.setCorreo(correo);
        entity.setCorreoVerificado(false);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(entity));

        // Act
        usuarioService.verificarCorreo(correo);

        // Assert
        assertTrue(entity.isCorreoVerificado());
        verify(usuarioRepository, times(1)).save(entity);
    }

    @Test
    void verificarCorreo_UsuarioNoExiste_LanzaUsuarioNoEncontradoException() {
        // Arrange
        String correo = "test@test.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(com.oficioya.exception.UsuarioNoEncontradoException.class, () -> {
            usuarioService.verificarCorreo(correo);
        });
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void verificarCorreo_UsuarioYaEstaVerificado_LanzaEstadoInvalidoException() {
        // Arrange
        String correo = "test@test.com";
        UsuarioEntity entity = new UsuarioEntity();
        entity.setCorreo(correo);
        entity.setCorreoVerificado(true);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThrows(com.oficioya.exception.EstadoInvalidoException.class, () -> {
            usuarioService.verificarCorreo(correo);
        });
        verify(usuarioRepository, never()).save(any());
    }

}