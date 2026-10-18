package com.oficioya.service.impl;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.domain.event.SolicitudEvent;
import com.oficioya.persistence.entity.EstadoSolicitud;
import com.oficioya.persistence.entity.SolicitudEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.SolicitudRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.mapper.SolicitudEntityMapper;
import com.oficioya.mapper.UsuarioEntityMapper;
import com.oficioya.validator.ISolicitudValidator;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceImplTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private SolicitudEntityMapper mapper;

    @Mock
    private UsuarioEntityMapper usuarioMapper;

    @Mock
    private ISolicitudValidator solicitudValidator;

    @InjectMocks
    private SolicitudServiceImpl solicitudService;

    private Solicitud solicitudDominio;
    private SolicitudEntity solicitudEntity;

    @BeforeEach
    void setUp() {
        solicitudDominio = new Solicitud();
        solicitudDominio.setId(1L);
        solicitudDominio.setEstadoEnum(EstadoSolicitud.CREADA);

        solicitudEntity = new SolicitudEntity();
        solicitudEntity.setId(1L);
        solicitudEntity.setEstado(EstadoSolicitud.CREADA);
    }

    @Test
    void testCrearSolicitud_Exito() {
        // Arrange
        Long contratanteId = 2L;
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        Usuario usuarioDominio = new Usuario();

        doNothing().when(solicitudValidator).validarContratante(contratanteId);
        when(usuarioRepository.findById(contratanteId)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioMapper.toDomain(usuarioEntity)).thenReturn(usuarioDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);
        when(solicitudRepository.save(solicitudEntity)).thenReturn(solicitudEntity);
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);

        // Act
        Solicitud resultado = solicitudService.crearSolicitud(solicitudDominio, contratanteId);

        // Assert
        assertNotNull(resultado);
        verify(solicitudRepository).save(any(SolicitudEntity.class));
        verify(solicitudValidator).validarContratante(contratanteId);
    }

    @Test
    void testCrearSolicitud_UsuarioNoEncontrado() {
        // Arrange
        Long contratanteId = 2L;
        doNothing().when(solicitudValidator).validarContratante(contratanteId);
        when(usuarioRepository.findById(contratanteId)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            solicitudService.crearSolicitud(solicitudDominio, contratanteId);
        });

        verify(solicitudRepository, never()).save(any());
    }

    @Test
    void testEnviarSolicitud_Exito() {
        // Arrange
        Long solicitudId = 1L;
        Long trabajadorId = 3L;

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        // Act
        solicitudService.enviarSolicitud(solicitudId, trabajadorId);

        // Assert
        verify(solicitudRepository).save(solicitudEntity);
        verify(eventPublisher).publishEvent(any(SolicitudEvent.class));
    }

    @Test
    void testAceptarSolicitud_Exito() {
        // Arrange
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.ENVIADA);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        // Act
        solicitudService.aceptarSolicitud(solicitudId);

        // Assert
        verify(solicitudRepository).save(solicitudEntity);
    }

    @Test
    void testRechazarSolicitud_Exito() {
        // Arrange
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.ENVIADA);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        // Act
        solicitudService.rechazarSolicitud(solicitudId);

        // Assert
        verify(solicitudRepository).save(solicitudEntity);
    }

    @Test
    void testIniciarSolicitud_Exito() {
        // Arrange
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.ACEPTADA);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        // Act
        solicitudService.iniciarSolicitud(solicitudId);

        // Assert
        verify(solicitudRepository).save(solicitudEntity);
    }

    @Test
    void testCompletarSolicitud_Exito() {
        // Arrange
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.EN_PROGRESO);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        // Act
        solicitudService.completarSolicitud(solicitudId);

        // Assert
        verify(solicitudRepository).save(solicitudEntity);
    }

    @Test
    void testCancelarSolicitud_Exito() {
        // Arrange
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.ACEPTADA);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        // Act
        solicitudService.cancelarSolicitud(solicitudId, "Ya no necesito el servicio");

        // Assert
        verify(solicitudRepository).save(solicitudEntity);
    }

    @Test
    void testCancelarSolicitud_DesdeCreadaLanzaEstadoInvalido() {
        // Arrange
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.CREADA);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);

        // Act
        // Assert
        assertThrows(com.oficioya.model.exception.EstadoInvalidoException.class, () -> {
            solicitudService.cancelarSolicitud(solicitudId, "Intento invalido");
        });
    }

    @Test
    void testRecuperarDominio_LanzaExcepcionSiNoExiste() {
        // Arrange
        when(solicitudRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            solicitudService.aceptarSolicitud(99L);
        });
    }

}
