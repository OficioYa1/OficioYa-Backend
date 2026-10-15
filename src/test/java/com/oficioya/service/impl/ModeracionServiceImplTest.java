package com.oficioYa.service.impl;

import com.oficioYa.exception.RecursoNoEncontradoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.model.dto.request.ReporteRequestDTO;
import com.oficioYa.model.dto.response.ReporteResponseDTO;
import com.oficioYa.persistence.entity.EstadoReporte;
import com.oficioYa.persistence.entity.ReporteEntity;
import com.oficioYa.persistence.entity.UsuarioEntity;
import com.oficioYa.repository.ReporteRepository;
import com.oficioYa.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeracionServiceImplTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ModeracionServiceImpl moderacionService;

    @Test
    @DisplayName("Debe reportar usuario exitosamente cuando reportador y reportado existen")
    void reportarUsuario_ConDatosValidos_DebeCrearReporte() {
        // Arrange
        Long reportadorId = 1L;
        Long reportadoId = 2L;
        ReporteRequestDTO request = new ReporteRequestDTO(reportadoId, "Incumplimiento de acuerdo", "https://evidencia.com/img1.png");

        UsuarioEntity reportador = UsuarioEntity.builder().id(reportadorId).correo("reportador@oficioya.com").build();
        UsuarioEntity reportado = UsuarioEntity.builder().id(reportadoId).correo("reportado@oficioya.com").build();

        ReporteEntity reporteGuardado = ReporteEntity.builder()
                .id(10L)
                .reportador(reportador)
                .reportado(reportado)
                .motivo(request.motivo())
                .evidenciaUrl(request.evidenciaUrl())
                .estado(EstadoReporte.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioRepository.findById(reportadorId)).thenReturn(Optional.of(reportador));
        when(usuarioRepository.findById(reportadoId)).thenReturn(Optional.of(reportado));
        when(reporteRepository.save(any(ReporteEntity.class))).thenReturn(reporteGuardado);

        // Act
        ReporteResponseDTO response = moderacionService.reportarUsuario(reportadorId, request);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(reportadorId, response.reportadorId());
        assertEquals(reportadoId, response.reportadoId());
        assertEquals("PENDIENTE", response.estado());
        assertEquals("Incumplimiento de acuerdo", response.motivo());
        verify(usuarioRepository).findById(reportadorId);
        verify(usuarioRepository).findById(reportadoId);
        verify(reporteRepository).save(any(ReporteEntity.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al reportar si el usuario reportador no existe")
    void reportarUsuario_ReportadorInexistente_DebeLanzarExcepcion() {
        // Arrange
        Long reportadorId = 99L;
        ReporteRequestDTO request = new ReporteRequestDTO(2L, "Motivo cualquiera", null);

        when(usuarioRepository.findById(reportadorId)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> moderacionService.reportarUsuario(reportadorId, request)
        );

        // Assert
        assertEquals("Reportador no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(reportadorId);
        verify(usuarioRepository, never()).findById(request.reportadoId());
        verifyNoInteractions(reporteRepository);
    }

    @Test
    @DisplayName("Debe lanzar excepción al reportar si el usuario reportado no existe")
    void reportarUsuario_ReportadoInexistente_DebeLanzarExcepcion() {
        // Arrange
        Long reportadorId = 1L;
        Long reportadoId = 99L;
        ReporteRequestDTO request = new ReporteRequestDTO(reportadoId, "Motivo", null);
        UsuarioEntity reportador = UsuarioEntity.builder().id(reportadorId).build();

        when(usuarioRepository.findById(reportadorId)).thenReturn(Optional.of(reportador));
        when(usuarioRepository.findById(reportadoId)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> moderacionService.reportarUsuario(reportadorId, request)
        );

        // Assert
        assertEquals("Usuario reportado no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(reportadorId);
        verify(usuarioRepository).findById(reportadoId);
        verifyNoInteractions(reporteRepository);
    }

    @Test
    @DisplayName("Debe consultar estado de reporte existente exitosamente")
    void consultarEstadoReporte_CuandoReporteExiste_DebeRetornarDTO() {
        // Arrange
        Long reporteId = 5L;
        UsuarioEntity reportador = UsuarioEntity.builder().id(1L).build();
        UsuarioEntity reportado = UsuarioEntity.builder().id(2L).build();
        ReporteEntity reporte = ReporteEntity.builder()
                .id(reporteId)
                .reportador(reportador)
                .reportado(reportado)
                .motivo("Spam recurrente")
                .estado(EstadoReporte.EN_REVISION)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(reporteRepository.findById(reporteId)).thenReturn(Optional.of(reporte));

        // Act
        ReporteResponseDTO response = moderacionService.consultarEstadoReporte(reporteId);

        // Assert
        assertNotNull(response);
        assertEquals(reporteId, response.id());
        assertEquals("EN_REVISION", response.estado());
        verify(reporteRepository).findById(reporteId);
    }

    @Test
    @DisplayName("Debe lanzar excepción al consultar reporte inexistente")
    void consultarEstadoReporte_CuandoReporteNoExiste_DebeLanzarExcepcion() {
        // Arrange
        Long reporteId = 404L;
        when(reporteRepository.findById(reporteId)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> moderacionService.consultarEstadoReporte(reporteId)
        );

        // Assert
        assertEquals("Reporte no encontrado", exception.getMessage());
        verify(reporteRepository).findById(reporteId);
    }

    @Test
    @DisplayName("Debe listar todos los reportes existentes")
    void obtenerTodosLosReportes_CuandoExistenReportes_DebeRetornarLista() {
        // Arrange
        UsuarioEntity user1 = UsuarioEntity.builder().id(1L).build();
        UsuarioEntity user2 = UsuarioEntity.builder().id(2L).build();
        ReporteEntity r1 = ReporteEntity.builder().id(1L).reportador(user1).reportado(user2).estado(EstadoReporte.PENDIENTE).build();
        ReporteEntity r2 = ReporteEntity.builder().id(2L).reportador(user2).reportado(user1).estado(EstadoReporte.RESUELTO).build();

        when(reporteRepository.findAll()).thenReturn(List.of(r1, r2));

        // Act
        List<ReporteResponseDTO> reportes = moderacionService.obtenerTodosLosReportes();

        // Assert
        assertNotNull(reportes);
        assertEquals(2, reportes.size());
        assertEquals(1L, reportes.get(0).id());
        assertEquals(2L, reportes.get(1).id());
        verify(reporteRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no existen reportes registrados")
    void obtenerTodosLosReportes_CuandoNoExistenReportes_DebeRetornarListaVacia() {
        // Arrange
        when(reporteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ReporteResponseDTO> reportes = moderacionService.obtenerTodosLosReportes();

        // Assert
        assertNotNull(reportes);
        assertTrue(reportes.isEmpty());
        verify(reporteRepository).findAll();
    }

    @Test
    @DisplayName("Debe pausar usuario correctamente marcando activo en false")
    void pausarUsuario_CuandoUsuarioExiste_DebeDesactivarUsuario() {
        // Arrange
        Long usuarioId = 10L;
        UsuarioEntity usuario = UsuarioEntity.builder()
                .id(usuarioId)
                .activo(true)
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Act
        moderacionService.pausarUsuario(usuarioId);

        // Assert
        assertFalse(usuario.isActivo());
        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe lanzar excepción al pausar si el usuario no existe")
    void pausarUsuario_CuandoUsuarioNoExiste_DebeLanzarExcepcion() {
        // Arrange
        Long usuarioId = 999L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> moderacionService.pausarUsuario(usuarioId)
        );

        // Assert
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository, never()).save(any());
    }
}
