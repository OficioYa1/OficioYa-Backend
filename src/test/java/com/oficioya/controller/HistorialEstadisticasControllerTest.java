package com.oficioya.controller;

import com.oficioya.model.dto.response.DashboardResumenDTO;
import com.oficioya.model.dto.response.EstadisticasResponseDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import com.oficioya.service.IHistorialEstadisticasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class HistorialEstadisticasControllerTest {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.oficioya.security.jwt.JwtService jwtService;

    @Mock
    private IHistorialEstadisticasService historialService;

    @InjectMocks
    private HistorialEstadisticasController controller;

    @Test
    void obtenerHistorialCliente_Success() {
        when(historialService.obtenerHistorialCliente(1L)).thenReturn(Arrays.asList(new SolicitudResponseDTO()));
        ResponseEntity<List<SolicitudResponseDTO>> res = controller.obtenerHistorialCliente(1L);
        assertEquals(200, res.getStatusCode().value());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void obtenerHistorialTrabajador_Success() {
        when(historialService.obtenerHistorialTrabajador(2L)).thenReturn(Arrays.asList(new SolicitudResponseDTO()));
        ResponseEntity<List<SolicitudResponseDTO>> res = controller.obtenerHistorialTrabajador(2L);
        assertEquals(200, res.getStatusCode().value());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void obtenerEstadisticasCliente_Success() {
        when(historialService.obtenerEstadisticasCliente(1L)).thenReturn(EstadisticasResponseDTO.builder().total(10).build());
        ResponseEntity<EstadisticasResponseDTO> res = controller.obtenerEstadisticasCliente(1L);
        assertEquals(200, res.getStatusCode().value());
        assertEquals(10L, res.getBody().getTotal());
    }

    @Test
    void obtenerEstadisticasTrabajador_Success() {
        when(historialService.obtenerEstadisticasTrabajador(2L)).thenReturn(EstadisticasResponseDTO.builder().total(20).build());
        ResponseEntity<EstadisticasResponseDTO> res = controller.obtenerEstadisticasTrabajador(2L);
        assertEquals(200, res.getStatusCode().value());
        assertEquals(20L, res.getBody().getTotal());
    }

    @Test
    void obtenerDashboardCliente_Success() {
        when(historialService.obtenerDashboardCliente(1L)).thenReturn(DashboardResumenDTO.builder().build());
        ResponseEntity<DashboardResumenDTO> res = controller.obtenerDashboardCliente(1L);
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void obtenerDashboardTrabajador_Success() {
        when(historialService.obtenerDashboardTrabajador(2L)).thenReturn(DashboardResumenDTO.builder().build());
        ResponseEntity<DashboardResumenDTO> res = controller.obtenerDashboardTrabajador(2L);
        assertEquals(200, res.getStatusCode().value());
    }
}
