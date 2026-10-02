package com.oficioya.service;

import com.oficioya.exception.CorreoYaRegistradoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.mapper.PerfilContratanteEntityMapper;
import com.oficioya.mapper.PerfilContratanteMapper;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.model.domain.PerfilContratante;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.persistence.entity.PerfilContratanteEntity;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilContratanteRepository;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.impl.UsuarioServiceImpl;
import com.oficioya.validator.IUsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private PerfilTrabajadorRepository perfilRepository;

    @Mock
    private PerfilTrabajadorEntityMapper perfilEntityMapper;

    @Mock
    private PerfilTrabajadorMapper perfilMapper;

    @Mock
    private PerfilContratanteRepository perfilContratanteRepository;

    @Mock
    private PerfilContratanteEntityMapper perfilContratanteEntityMapper;

    @Mock
    private PerfilContratanteMapper perfilContratanteMapper;

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

    // ══════════════════════════════════════════════════════════════════
    //  RF-49  Registrar cuenta de usuario
    // ══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("registrarUsuario - correo nuevo registra exitosamente y retorna dominio")
    void registrarUsuario_correoNuevo_registraExitosamente() {
        // Arrange
        doNothing().when(usuarioValidator).validarCorreoUnico(domainUsuario.getCorreo());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(perfilContratanteRepository.save(any(PerfilContratanteEntity.class))).thenReturn(new PerfilContratanteEntity());
        when(entityMapper.toDomain(usuarioEntity)).thenReturn(domainUsuario);

        // Act
        Usuario result = usuarioService.registrarUsuario(domainUsuario);

        // Assert
        assertNotNull(result);
        assertEquals("juan@test.com", result.getCorreo());
        assertEquals(RolUsuario.CONTRATANTE, result.getRol());
        verify(usuarioValidator, times(1)).validarCorreoUnico(domainUsuario.getCorreo());
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("registrarUsuario - correo duplicado lanza CorreoYaRegistradoException")
    void registrarUsuario_correoDuplicado_lanzaCorreoYaRegistradoException() {
        // Arrange
        doThrow(new CorreoYaRegistradoException("El correo juan@test.com ya se encuentra registrado."))
                .when(usuarioValidator).validarCorreoUnico(domainUsuario.getCorreo());

        // Act & Assert
        CorreoYaRegistradoException ex = assertThrows(
                CorreoYaRegistradoException.class,
                () -> usuarioService.registrarUsuario(domainUsuario)
        );
        assertEquals("El correo juan@test.com ya se encuentra registrado.", ex.getMessage());
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
        verify(entityMapper, never()).toEntity(any(Usuario.class));
    }

    @Test
    @DisplayName("registrarUsuario - usuario nuevo tiene valores por defecto correctos")
    void registrarUsuario_usuarioNuevo_tieneValoresPorDefectoCorrectos() {
        // Arrange
        doNothing().when(usuarioValidator).validarCorreoUnico(any());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(perfilContratanteRepository.save(any(PerfilContratanteEntity.class))).thenReturn(new PerfilContratanteEntity());
        when(entityMapper.toDomain(any(UsuarioEntity.class))).thenReturn(domainUsuario);

        // Act
        usuarioService.registrarUsuario(domainUsuario);

        // Assert
        assertFalse(domainUsuario.isCorreoVerificado(), "El correo NO debe estar verificado al registrarse");
        assertFalse(domainUsuario.isTelefonoVerificado(), "El telefono NO debe estar verificado al registrarse");
        assertTrue(domainUsuario.isActivo(), "El usuario debe estar activo al registrarse");
        assertNotNull(domainUsuario.getFechaRegistro(), "La fecha de registro debe establecerse");
    }

    @Test
    @DisplayName("registrarUsuario - fallo en repositorio propaga RuntimeException")
    void registrarUsuario_falloEnRepositorio_propagaRuntimeException() {
        // Arrange
        doNothing().when(usuarioValidator).validarCorreoUnico(any());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class)))
                .thenThrow(new RuntimeException("Error de conexion con la base de datos"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.registrarUsuario(domainUsuario)
        );
        assertTrue(ex.getMessage().contains("Error de conexion"));
    }

    @Test
    @DisplayName("registrarUsuario - contratante crea perfil vacio automaticamente")
    void registrarUsuario_contratante_creaPerfilVacioAutomaticamente() {
        // Arrange
        domainUsuario.setRol(RolUsuario.CONTRATANTE);
        usuarioEntity.setRol(RolUsuario.CONTRATANTE);
        doNothing().when(usuarioValidator).validarCorreoUnico(any());
        when(entityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(perfilContratanteRepository.save(any(PerfilContratanteEntity.class))).thenReturn(new PerfilContratanteEntity());
        when(entityMapper.toDomain(usuarioEntity)).thenReturn(domainUsuario);

        // Act
        usuarioService.registrarUsuario(domainUsuario);

        // Assert
        verify(entityMapper, times(1)).toEntity(any(Usuario.class));
        verify(entityMapper, times(1)).toDomain(usuarioEntity);
        verify(perfilContratanteRepository, times(1)).save(any(PerfilContratanteEntity.class));
    }

    // ══════════════════════════════════════════════════════════════════
    //  RF-40  Verificar correo electronico
    // ══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("verificarCorreo - usuario existe y no verificado marca como verificado y guarda")
    void verificarCorreo_usuarioExisteYNoVerificado_marcaVerificadoYGuarda() {
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
    @DisplayName("verificarCorreo - usuario no existe lanza UsuarioNoEncontradoException")
    void verificarCorreo_usuarioNoExiste_lanzaUsuarioNoEncontradoException() {
        // Arrange
        String correo = "test@test.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class,
                () -> usuarioService.verificarCorreo(correo));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("verificarCorreo - correo ya verificado lanza EstadoInvalidoException")
    void verificarCorreo_correoYaVerificado_lanzaEstadoInvalidoException() {
        // Arrange
        String correo = "test@test.com";
        UsuarioEntity entity = new UsuarioEntity();
        entity.setCorreo(correo);
        entity.setCorreoVerificado(true);
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThrows(EstadoInvalidoException.class,
                () -> usuarioService.verificarCorreo(correo));
        verify(usuarioRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════════
    //  RF-52  Consultar perfil de trabajador
    // ══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPerfilTrabajador - usuario trabajador existente retorna DTO con datos")
    void obtenerPerfilTrabajador_usuarioTrabajadorExistente_retornaDTOConDatos() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setRol(RolUsuario.TRABAJADOR);

        PerfilTrabajadorEntity perfilEntity = new PerfilTrabajadorEntity();
        perfilEntity.setId(10L);
        perfilEntity.setUsuario(usuario);
        perfilEntity.setZonaCobertura("Norte");

        PerfilTrabajador perfilDomain = new PerfilTrabajador();
        perfilDomain.setId(10L);

        PerfilTrabajadorResponseDTO responseDTO = new PerfilTrabajadorResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setZonaCobertura("Norte");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfilEntity));
        when(perfilEntityMapper.toDomain(perfilEntity)).thenReturn(perfilDomain);
        when(perfilMapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        // Act
        PerfilTrabajadorResponseDTO result = usuarioService.obtenerPerfilTrabajador(1L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Norte", result.getZonaCobertura());
    }

    @Test
    @DisplayName("obtenerPerfilTrabajador - usuario inexistente lanza UsuarioNoEncontradoException")
    void obtenerPerfilTrabajador_usuarioInexistente_lanzaUsuarioNoEncontradoException() {
        // Arrange
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class,
                () -> usuarioService.obtenerPerfilTrabajador(99L));
    }

    @Test
    @DisplayName("obtenerPerfilTrabajador - usuario no es trabajador lanza EstadoInvalidoException")
    void obtenerPerfilTrabajador_usuarioNoEsTrabajador_lanzaEstadoInvalidoException() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(2L);
        usuario.setRol(RolUsuario.CONTRATANTE);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(EstadoInvalidoException.class,
                () -> usuarioService.obtenerPerfilTrabajador(2L));
    }

    @Test
    @DisplayName("obtenerPerfilTrabajador - perfil no configurado lanza RecursoNoEncontradoException")
    void obtenerPerfilTrabajador_perfilNoConfigurado_lanzaRecursoNoEncontradoException() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(3L);
        usuario.setRol(RolUsuario.TRABAJADOR);
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuarioId(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioService.obtenerPerfilTrabajador(3L));
    }

    // ══════════════════════════════════════════════════════════════════
    //  RF-53  Consultar perfil de contratante
    // ══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("obtenerPerfilContratante - usuario contratante existente retorna DTO con calificacion")
    void obtenerPerfilContratante_usuarioContratanteExistente_retornaDTOConCalificacion() {
        // Arrange
        Long usuarioId = 1L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setRol(RolUsuario.CONTRATANTE);

        PerfilContratanteEntity perfilEntity = new PerfilContratanteEntity();
        perfilEntity.setId(10L);
        perfilEntity.setCalificacionPromedio(4.5);

        PerfilContratante perfilDomain = new PerfilContratante();
        PerfilContratanteResponseDTO responseDTO = new PerfilContratanteResponseDTO();
        responseDTO.setCalificacionPromedio(4.5);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perfilContratanteRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(perfilEntity));
        when(perfilContratanteEntityMapper.toDomain(perfilEntity)).thenReturn(perfilDomain);
        when(perfilContratanteMapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        // Act
        PerfilContratanteResponseDTO result = usuarioService.obtenerPerfilContratante(usuarioId);

        // Assert
        assertNotNull(result);
        assertEquals(4.5, result.getCalificacionPromedio());
        verify(perfilContratanteRepository).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("obtenerPerfilContratante - usuario inexistente lanza UsuarioNoEncontradoException")
    void obtenerPerfilContratante_usuarioInexistente_lanzaUsuarioNoEncontradoException() {
        // Arrange
        Long usuarioId = 99L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class,
                () -> usuarioService.obtenerPerfilContratante(usuarioId));
        verify(perfilContratanteRepository, never()).findByUsuarioId(anyLong());
    }

    @Test
    @DisplayName("obtenerPerfilContratante - usuario no es contratante lanza EstadoInvalidoException")
    void obtenerPerfilContratante_usuarioNoEsContratante_lanzaEstadoInvalidoException() {
        // Arrange
        Long usuarioId = 2L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setRol(RolUsuario.TRABAJADOR);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(EstadoInvalidoException.class,
                () -> usuarioService.obtenerPerfilContratante(usuarioId));
        verify(perfilContratanteRepository, never()).findByUsuarioId(anyLong());
    }

    @Test
    @DisplayName("obtenerPerfilContratante - perfil no configurado lanza RecursoNoEncontradoException")
    void obtenerPerfilContratante_perfilNoConfigurado_lanzaRecursoNoEncontradoException() {
        // Arrange
        Long usuarioId = 3L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setRol(RolUsuario.CONTRATANTE);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perfilContratanteRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioService.obtenerPerfilContratante(usuarioId));
    }


    // =========================================================================
    // TESTS PARA RF-57: EDITAR INFORMACIÓN BÁSICA DEL PERFIL
    // =========================================================================

    @Test
    @DisplayName("editarPerfilTrabajador - Flujo Exitoso - Actualiza teléfono y zona")
    void editarPerfilTrabajador_flujoExitoso_actualizaDatos() {
        // Arrange
        Long usuarioId = 1L;
        com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO request = 
            new com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO("3001234567", "Norte de la ciudad");

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setRol(RolUsuario.TRABAJADOR);
        usuario.setTelefono("0000000000");

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setUsuario(usuario);
        perfil.setZonaCobertura("Sin definir");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(perfil));

        // Act
        usuarioService.editarPerfilTrabajador(usuarioId, request);

        // Assert
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("Norte de la ciudad", perfil.getZonaCobertura());
        verify(usuarioRepository, times(1)).save(usuario);
        verify(perfilRepository, times(1)).save(perfil);
    }

    @Test
    @DisplayName("editarPerfilTrabajador - Rol Incorrecto - Lanza EstadoInvalidoException")
    void editarPerfilTrabajador_rolIncorrecto_lanzaExcepcion() {
        // Arrange
        Long usuarioId = 2L;
        com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO request = 
            new com.oficioya.model.dto.request.EditarPerfilTrabajadorRequestDTO("3001234567", "Sur");

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setRol(RolUsuario.CONTRATANTE); // Rol incorrecto

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(EstadoInvalidoException.class, () -> {
            usuarioService.editarPerfilTrabajador(usuarioId, request);
        });
        verify(usuarioRepository, never()).save(any());
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("editarPerfilContratante - Flujo Exitoso - Actualiza teléfono y descripción")
    void editarPerfilContratante_flujoExitoso_actualizaDatos() {
        // Arrange
        Long usuarioId = 3L;
        com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO request = 
            new com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO("3119876543", "Busco plomero urgente");

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setRol(RolUsuario.CONTRATANTE);
        usuario.setTelefono("1111111111");

        PerfilContratanteEntity perfil = new PerfilContratanteEntity();
        perfil.setUsuario(usuario);
        perfil.setDescripcion(null);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perfilContratanteRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(perfil));

        // Act
        usuarioService.editarPerfilContratante(usuarioId, request);

        // Assert
        assertEquals("3119876543", usuario.getTelefono());
        assertEquals("Busco plomero urgente", perfil.getDescripcion());
        verify(usuarioRepository, times(1)).save(usuario);
        verify(perfilContratanteRepository, times(1)).save(perfil);
    }

    @Test
    @DisplayName("editarPerfilContratante - Usuario No Encontrado - Lanza UsuarioNoEncontradoException")
    void editarPerfilContratante_usuarioNoExiste_lanzaExcepcion() {
        // Arrange
        Long usuarioId = 99L;
        com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO request = 
            new com.oficioya.model.dto.request.EditarPerfilContratanteRequestDTO("3119876543", "Test");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.editarPerfilContratante(usuarioId, request);
        });
        verify(usuarioRepository, never()).save(any());
    }


    // =========================================================================
    // TESTS PARA RF-58: ELIMINAR CUENTA (BORRADO LÓGICO)
    // =========================================================================

    @Test
    @DisplayName("eliminarCuenta - Flujo Exitoso - Cambia estado a inactivo")
    void eliminarCuenta_flujoExitoso_inactivaUsuario() {
        // Arrange
        Long usuarioId = 1L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setActivo(true); // Activo inicialmente

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Act
        usuarioService.eliminarCuenta(usuarioId);

        // Assert
        assertFalse(usuario.isActivo()); // Debe pasar a false
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("eliminarCuenta - Usuario no existe - Lanza UsuarioNoEncontradoException")
    void eliminarCuenta_usuarioInexistente_lanzaExcepcion() {
        // Arrange
        Long usuarioId = 99L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.eliminarCuenta(usuarioId);
        });
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminarCuenta - Usuario ya está inactivo - Lanza EstadoInvalidoException")
    void eliminarCuenta_usuarioYaInactivo_lanzaExcepcion() {
        // Arrange
        Long usuarioId = 2L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        usuario.setActivo(false); // Ya está inactivo

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(EstadoInvalidoException.class, () -> {
            usuarioService.eliminarCuenta(usuarioId);
        });
        verify(usuarioRepository, never()).save(any());
    }
}
