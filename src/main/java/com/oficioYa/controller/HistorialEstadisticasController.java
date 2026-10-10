package com.oficioYa.controller;

import com.oficioYa.controller.docs.HistorialEstadisticasApi;
import com.oficioYa.model.dto.response.DashboardResumenDTO;
import com.oficioYa.model.dto.response.EstadisticasResponseDTO;
import com.oficioYa.model.dto.response.SolicitudResponseDTO;
import com.oficioYa.service.IHistorialEstadisticasService;
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
