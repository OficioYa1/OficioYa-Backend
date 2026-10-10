package com.oficioYa.service;

import com.oficioYa.model.dto.response.DashboardResumenDTO;
import com.oficioYa.model.dto.response.EstadisticasResponseDTO;
import com.oficioYa.model.dto.response.SolicitudResponseDTO;

import java.util.List;

public interface IHistorialEstadisticasService {
    
    // RF-44
    List<SolicitudResponseDTO> obtenerHistorialCliente(Long clienteId);
    
    // RF-45
    List<SolicitudResponseDTO> obtenerHistorialTrabajador(Long trabajadorId);
    
    // RF-46
    EstadisticasResponseDTO obtenerEstadisticasCliente(Long clienteId);
    
    // RF-47
    EstadisticasResponseDTO obtenerEstadisticasTrabajador(Long trabajadorId);
    
    // RF-48
    DashboardResumenDTO obtenerDashboardCliente(Long clienteId);
    DashboardResumenDTO obtenerDashboardTrabajador(Long trabajadorId);
}
