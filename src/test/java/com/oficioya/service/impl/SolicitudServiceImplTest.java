package com.oficioYa.service.impl;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.model.domain.Usuario;
import com.oficioYa.model.domain.event.SolicitudEvent;
import com.oficioYa.persistence.entity.EstadoSolicitud;
import com.oficioYa.persistence.entity.SolicitudEntity;
import com.oficioYa.persistence.entity.UsuarioEntity;
import com.oficioYa.repository.SolicitudRepository;
import com.oficioYa.repository.UsuarioRepository;
import com.oficioYa.mapper.SolicitudEntityMapper;
import com.oficioYa.mapper.UsuarioEntityMapper;
import com.oficioYa.validator.ISolicitudValidator;
import com.oficioYa.exception.RecursoNoEncontradoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;

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
        Long contratanteId = 2L;
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        Usuario usuarioDominio = new Usuario();

        doNothing().when(solicitudValidator).validarContratante(contratanteId);
        when(usuarioRepository.findById(contratanteId)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioMapper.toDomain(usuarioEntity)).thenReturn(usuarioDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);
        when(solicitudRepository.save(solicitudEntity)).thenReturn(solicitudEntity);
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);

        Solicitud resultado = solicitudService.crearSolicitud(solicitudDominio, contratanteId);

        assertNotNull(resultado);
        verify(solicitudRepository).save(any(SolicitudEntity.class));
        verify(solicitudValidator).validarContratante(contratanteId);
    }

    @Test
    void testCrearSolicitud_UsuarioNoEncontrado() {
        Long contratanteId = 2L;
        doNothing().when(solicitudValidator).validarContratante(contratanteId);
        when(usuarioRepository.findById(contratanteId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            solicitudService.crearSolicitud(solicitudDominio, contratanteId);
        });

        verify(solicitudRepository, never()).save(any());
    }

    @Test
    void testEnviarSolicitud_Exito() {
        Long solicitudId = 1L;
        Long trabajadorId = 3L;

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        solicitudService.enviarSolicitud(solicitudId, trabajadorId);

        verify(solicitudRepository).save(solicitudEntity);
        verify(eventPublisher).publishEvent(any(SolicitudEvent.class));
    }

    @Test
    void testAceptarSolicitud_Exito() {
        Long solicitudId = 1L;
        solicitudDominio.setEstadoEnum(EstadoSolicitud.ENVIADA);

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(solicitudEntity));
        when(mapper.toDomain(solicitudEntity)).thenReturn(solicitudDominio);
        when(mapper.toEntity(solicitudDominio)).thenReturn(solicitudEntity);

        solicitudService.aceptarSolicitud(solicitudId);

        verify(solicitudRepository).save(solicitudEntity);
    }

    @Test
    void testRecuperarDominio_LanzaExcepcionSiNoExiste() {
        when(solicitudRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            solicitudService.aceptarSolicitud(99L);
        });
    }

}
