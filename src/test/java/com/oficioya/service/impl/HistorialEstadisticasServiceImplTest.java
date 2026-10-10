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
        when(solicitudRepository.findByContratanteIdOrderByFechaCreacionDesc(1L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any(SolicitudEntity.class))).thenReturn(domain);
        when(dtoMapper.toResponse(any(Solicitud.class))).thenReturn(dto);

        List<SolicitudResponseDTO> res = historialService.obtenerHistorialCliente(1L);

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(solicitudRepository, times(1)).findByContratanteIdOrderByFechaCreacionDesc(1L);
    }

    @Test
    void obtenerHistorialTrabajador_Success() {
        when(solicitudRepository.findByTrabajadorIdOrderByFechaCreacionDesc(2L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any(SolicitudEntity.class))).thenReturn(domain);
        when(dtoMapper.toResponse(any(Solicitud.class))).thenReturn(dto);

        List<SolicitudResponseDTO> res = historialService.obtenerHistorialTrabajador(2L);

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(solicitudRepository, times(1)).findByTrabajadorIdOrderByFechaCreacionDesc(2L);
    }

    @Test
    void obtenerEstadisticasCliente_Success() {
        when(solicitudRepository.countByContratanteId(1L)).thenReturn(10L);
        when(solicitudRepository.countByContratanteIdAndEstado(1L, EstadoSolicitud.COMPLETADA)).thenReturn(5L);
        when(solicitudRepository.countByContratanteIdAndEstado(1L, EstadoSolicitud.CANCELADA)).thenReturn(2L);
        when(solicitudRepository.countByContratanteIdAndEstado(1L, EstadoSolicitud.EN_PROGRESO)).thenReturn(1L);

        EstadisticasResponseDTO stats = historialService.obtenerEstadisticasCliente(1L);

        assertNotNull(stats);
        assertEquals(10L, stats.getTotal());
        assertEquals(5L, stats.getCompletadas());
        assertEquals(2L, stats.getCanceladas());
        assertEquals(1L, stats.getEnProgreso());
        assertEquals(2L, stats.getPendientes()); // 10 - 5 - 2 - 1 = 2
    }

    @Test
    void obtenerEstadisticasTrabajador_Success() {
        when(solicitudRepository.countByTrabajadorId(2L)).thenReturn(20L);
        when(solicitudRepository.countByTrabajadorIdAndEstado(2L, EstadoSolicitud.COMPLETADA)).thenReturn(15L);
        when(solicitudRepository.countByTrabajadorIdAndEstado(2L, EstadoSolicitud.CANCELADA)).thenReturn(1L);
        when(solicitudRepository.countByTrabajadorIdAndEstado(2L, EstadoSolicitud.EN_PROGRESO)).thenReturn(2L);

        EstadisticasResponseDTO stats = historialService.obtenerEstadisticasTrabajador(2L);

        assertNotNull(stats);
        assertEquals(20L, stats.getTotal());
        assertEquals(15L, stats.getCompletadas());
        assertEquals(1L, stats.getCanceladas());
        assertEquals(2L, stats.getEnProgreso());
        assertEquals(2L, stats.getPendientes()); // 20 - 15 - 1 - 2 = 2
    }

    @Test
    void obtenerDashboardCliente_Success() {
        // Mocks for estadisticas
        when(solicitudRepository.countByContratanteId(1L)).thenReturn(10L);
        // Mocks for historial
        when(solicitudRepository.findByContratanteIdOrderByFechaCreacionDesc(1L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any())).thenReturn(domain);
        when(dtoMapper.toResponse(any())).thenReturn(dto);

        DashboardResumenDTO dashboard = historialService.obtenerDashboardCliente(1L);

        assertNotNull(dashboard);
        assertNotNull(dashboard.getEstadisticas());
        assertNotNull(dashboard.getHistorialReciente());
        assertEquals(1, dashboard.getHistorialReciente().size());
    }

    @Test
    void obtenerDashboardTrabajador_Success() {
        // Mocks for estadisticas
        when(solicitudRepository.countByTrabajadorId(2L)).thenReturn(10L);
        // Mocks for historial
        when(solicitudRepository.findByTrabajadorIdOrderByFechaCreacionDesc(2L))
                .thenReturn(Arrays.asList(entity));
        when(entityMapper.toDomain(any())).thenReturn(domain);
        when(dtoMapper.toResponse(any())).thenReturn(dto);

        DashboardResumenDTO dashboard = historialService.obtenerDashboardTrabajador(2L);

        assertNotNull(dashboard);
        assertNotNull(dashboard.getEstadisticas());
        assertNotNull(dashboard.getHistorialReciente());
        assertEquals(1, dashboard.getHistorialReciente().size());
    }
}
