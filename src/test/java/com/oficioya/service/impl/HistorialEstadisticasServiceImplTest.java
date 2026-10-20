package com.oficioya.service.impl;

import com.oficioya.mapper.SolicitudEntityMapper;
import com.oficioya.mapper.SolicitudMapper;
import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.dto.response.DashboardResumenDTO;
import com.oficioya.model.dto.response.EstadisticasResponseDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import com.oficioya.persistence.entity.EstadoSolicitud;
import com.oficioya.persistence.entity.SolicitudEntity;
import com.oficioya.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialEstadisticasServiceImplTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private SolicitudEntityMapper entityMapper;

    @Mock
    private SolicitudMapper dtoMapper;

    @InjectMocks
    private HistorialEstadisticasServiceImpl historialService;

    private SolicitudEntity entity;
    private Solicitud domain;
    private SolicitudResponseDTO dto;

    @BeforeEach
    void setUp() {
        entity = new SolicitudEntity();
        entity.setId(1L);

        domain = new Solicitud();
        
        dto = new SolicitudResponseDTO();
        dto.setId(1L);
    }

    @Test
    void obtenerHistorialCliente_Success() {
        // Arrange
        when(solicitudRepository.findByContratanteIdOrderByFechaCreacionDesc(1L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any(SolicitudEntity.class))).thenReturn(domain);
        when(dtoMapper.toResponse(any(Solicitud.class))).thenReturn(dto);

        // Act
        List<SolicitudResponseDTO> res = historialService.obtenerHistorialCliente(1L);

        // Assert
        assertNotNull(res);
        assertEquals(1, res.size());
        verify(solicitudRepository, times(1)).findByContratanteIdOrderByFechaCreacionDesc(1L);
    }

    @Test
    void obtenerHistorialCliente_ListaVacia() {
        // Arrange
        when(solicitudRepository.findByContratanteIdOrderByFechaCreacionDesc(99L))
                .thenReturn(List.of());

        // Act
        List<SolicitudResponseDTO> res = historialService.obtenerHistorialCliente(99L);

        // Assert
        assertNotNull(res);
        assertTrue(res.isEmpty());
        verify(solicitudRepository, times(1)).findByContratanteIdOrderByFechaCreacionDesc(99L);
    }

    @Test
    void obtenerHistorialTrabajador_Success() {
        // Arrange
        when(solicitudRepository.findByTrabajadorIdOrderByFechaCreacionDesc(2L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any(SolicitudEntity.class))).thenReturn(domain);
        when(dtoMapper.toResponse(any(Solicitud.class))).thenReturn(dto);

        // Act
        List<SolicitudResponseDTO> res = historialService.obtenerHistorialTrabajador(2L);

        // Assert
        assertNotNull(res);
        assertEquals(1, res.size());
        verify(solicitudRepository, times(1)).findByTrabajadorIdOrderByFechaCreacionDesc(2L);
    }

    @Test
    void obtenerEstadisticasCliente_Success() {
        // Arrange
        when(solicitudRepository.countByContratanteId(1L)).thenReturn(10L);
        when(solicitudRepository.countByContratanteIdAndEstado(1L, EstadoSolicitud.COMPLETADA)).thenReturn(5L);
        when(solicitudRepository.countByContratanteIdAndEstado(1L, EstadoSolicitud.CANCELADA)).thenReturn(2L);
        when(solicitudRepository.countByContratanteIdAndEstado(1L, EstadoSolicitud.EN_PROGRESO)).thenReturn(1L);

        // Act
        EstadisticasResponseDTO stats = historialService.obtenerEstadisticasCliente(1L);

        // Assert
        assertNotNull(stats);
        assertEquals(10L, stats.getTotal());
        assertEquals(5L, stats.getCompletadas());
        assertEquals(2L, stats.getCanceladas());
        assertEquals(1L, stats.getEnProgreso());
        assertEquals(2L, stats.getPendientes()); // 10 - 5 - 2 - 1 = 2
    }

    @Test
    void obtenerEstadisticasTrabajador_Success() {
        // Arrange
        when(solicitudRepository.countByTrabajadorId(2L)).thenReturn(20L);
        when(solicitudRepository.countByTrabajadorIdAndEstado(2L, EstadoSolicitud.COMPLETADA)).thenReturn(15L);
        when(solicitudRepository.countByTrabajadorIdAndEstado(2L, EstadoSolicitud.CANCELADA)).thenReturn(1L);
        when(solicitudRepository.countByTrabajadorIdAndEstado(2L, EstadoSolicitud.EN_PROGRESO)).thenReturn(2L);

        // Act
        EstadisticasResponseDTO stats = historialService.obtenerEstadisticasTrabajador(2L);

        // Assert
        assertNotNull(stats);
        assertEquals(20L, stats.getTotal());
        assertEquals(15L, stats.getCompletadas());
        assertEquals(1L, stats.getCanceladas());
        assertEquals(2L, stats.getEnProgreso());
        assertEquals(2L, stats.getPendientes()); // 20 - 15 - 1 - 2 = 2
    }

    @Test
    void obtenerDashboardCliente_Success() {
        // Arrange
        when(solicitudRepository.countByContratanteId(1L)).thenReturn(10L);
        when(solicitudRepository.findByContratanteIdOrderByFechaCreacionDesc(1L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any())).thenReturn(domain);
        when(dtoMapper.toResponse(any())).thenReturn(dto);

        // Act
        DashboardResumenDTO dashboard = historialService.obtenerDashboardCliente(1L);

        // Assert
        assertNotNull(dashboard);
        assertNotNull(dashboard.getEstadisticas());
        assertNotNull(dashboard.getHistorialReciente());
        assertEquals(1, dashboard.getHistorialReciente().size());
    }

    @Test
    void obtenerDashboardTrabajador_Success() {
        // Arrange
        when(solicitudRepository.countByTrabajadorId(2L)).thenReturn(10L);
        when(solicitudRepository.findByTrabajadorIdOrderByFechaCreacionDesc(2L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any())).thenReturn(domain);
        when(dtoMapper.toResponse(any())).thenReturn(dto);

        // Act
        DashboardResumenDTO dashboard = historialService.obtenerDashboardTrabajador(2L);

        // Assert
        assertNotNull(dashboard);
        assertNotNull(dashboard.getEstadisticas());
        assertNotNull(dashboard.getHistorialReciente());
        assertEquals(1, dashboard.getHistorialReciente().size());
    }
}
