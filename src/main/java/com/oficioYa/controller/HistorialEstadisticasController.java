package com.oficioya.controller;

import com.oficioya.controller.docs.HistorialEstadisticasApi;
import com.oficioya.model.dto.response.DashboardResumenDTO;
import com.oficioya.model.dto.response.EstadisticasResponseDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import com.oficioya.service.IHistorialEstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historial-estadisticas")
@RequiredArgsConstructor
public class HistorialEstadisticasController implements HistorialEstadisticasApi {

    private final IHistorialEstadisticasService historialEstadisticasService;

    @Override
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<SolicitudResponseDTO>> obtenerHistorialCliente(Long clienteId) {
        return ResponseEntity.ok(historialEstadisticasService.obtenerHistorialCliente(clienteId));
    }

    @Override
    @PreAuthorize("hasRole('TRABAJADOR')")
    public ResponseEntity<List<SolicitudResponseDTO>> obtenerHistorialTrabajador(Long trabajadorId) {
        return ResponseEntity.ok(historialEstadisticasService.obtenerHistorialTrabajador(trabajadorId));
    }

    @Override
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<EstadisticasResponseDTO> obtenerEstadisticasCliente(Long clienteId) {
        return ResponseEntity.ok(historialEstadisticasService.obtenerEstadisticasCliente(clienteId));
    }

    @Override
    @PreAuthorize("hasRole('TRABAJADOR')")
    public ResponseEntity<EstadisticasResponseDTO> obtenerEstadisticasTrabajador(Long trabajadorId) {
        return ResponseEntity.ok(historialEstadisticasService.obtenerEstadisticasTrabajador(trabajadorId));
    }

    @Override
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DashboardResumenDTO> obtenerDashboardCliente(Long clienteId) {
        return ResponseEntity.ok(historialEstadisticasService.obtenerDashboardCliente(clienteId));
    }

    @Override
    @PreAuthorize("hasRole('TRABAJADOR')")
    public ResponseEntity<DashboardResumenDTO> obtenerDashboardTrabajador(Long trabajadorId) {
        return ResponseEntity.ok(historialEstadisticasService.obtenerDashboardTrabajador(trabajadorId));
    }
}
