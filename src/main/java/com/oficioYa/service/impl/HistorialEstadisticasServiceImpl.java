package com.oficioYa.service.impl;

import com.oficioYa.mapper.SolicitudEntityMapper;
import com.oficioYa.mapper.SolicitudMapper;
import com.oficioYa.model.dto.response.DashboardResumenDTO;
import com.oficioYa.model.dto.response.EstadisticasResponseDTO;
import com.oficioYa.model.dto.response.SolicitudResponseDTO;
import com.oficioYa.persistence.entity.EstadoSolicitud;
import com.oficioYa.persistence.entity.SolicitudEntity;
import com.oficioYa.repository.SolicitudRepository;
import com.oficioYa.service.IHistorialEstadisticasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistorialEstadisticasServiceImpl implements IHistorialEstadisticasService {

    private final SolicitudRepository solicitudRepository;
    private final SolicitudEntityMapper entityMapper;
    private final SolicitudMapper dtoMapper;

    private SolicitudResponseDTO mapearASolicitudResponseDTO(SolicitudEntity entity) {
        return dtoMapper.toResponse(entityMapper.toDomain(entity));
    }

    @Override
    public List<SolicitudResponseDTO> obtenerHistorialCliente(Long clienteId) {
        log.info("Obteniendo historial de solicitudes para cliente {}", clienteId);
        return solicitudRepository.findByContratanteIdOrderByFechaCreacionDesc(clienteId)
                .stream()
                .map(this::mapearASolicitudResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudResponseDTO> obtenerHistorialTrabajador(Long trabajadorId) {
        log.info("Obteniendo historial de trabajos para trabajador {}", trabajadorId);
        return solicitudRepository.findByTrabajadorIdOrderByFechaCreacionDesc(trabajadorId)
                .stream()
                .map(this::mapearASolicitudResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EstadisticasResponseDTO obtenerEstadisticasCliente(Long clienteId) {
        log.info("Calculando estadisticas para cliente {}", clienteId);
        long total = solicitudRepository.countByContratanteId(clienteId);
        long completadas = solicitudRepository.countByContratanteIdAndEstado(clienteId, EstadoSolicitud.COMPLETADA);
        long canceladas = solicitudRepository.countByContratanteIdAndEstado(clienteId, EstadoSolicitud.CANCELADA);
        long enProgreso = solicitudRepository.countByContratanteIdAndEstado(clienteId, EstadoSolicitud.EN_PROGRESO);
        long pendientes = total - (completadas + canceladas + enProgreso);

        return EstadisticasResponseDTO.builder()
                .total(total)
                .completadas(completadas)
                .canceladas(canceladas)
                .enProgreso(enProgreso)
                .pendientes(pendientes)
                .build();
    }

    @Override
    public EstadisticasResponseDTO obtenerEstadisticasTrabajador(Long trabajadorId) {
        log.info("Calculando estadisticas para trabajador {}", trabajadorId);
        long total = solicitudRepository.countByTrabajadorId(trabajadorId);
        long completadas = solicitudRepository.countByTrabajadorIdAndEstado(trabajadorId, EstadoSolicitud.COMPLETADA);
        long canceladas = solicitudRepository.countByTrabajadorIdAndEstado(trabajadorId, EstadoSolicitud.CANCELADA);
        long enProgreso = solicitudRepository.countByTrabajadorIdAndEstado(trabajadorId, EstadoSolicitud.EN_PROGRESO);
        long pendientes = total - (completadas + canceladas + enProgreso);

        return EstadisticasResponseDTO.builder()
                .total(total)
                .completadas(completadas)
                .canceladas(canceladas)
                .enProgreso(enProgreso)
                .pendientes(pendientes)
                .build();
    }

    @Override
    public DashboardResumenDTO obtenerDashboardCliente(Long clienteId) {
        EstadisticasResponseDTO estadisticas = obtenerEstadisticasCliente(clienteId);
        List<SolicitudResponseDTO> historial = obtenerHistorialCliente(clienteId);
        
        // Limitar a los ultimos 5 para el dashboard
        List<SolicitudResponseDTO> historialReciente = historial.stream()
                .limit(5)
                .collect(Collectors.toList());

        return DashboardResumenDTO.builder()
                .estadisticas(estadisticas)
                .historialReciente(historialReciente)
                .build();
    }

    @Override
    public DashboardResumenDTO obtenerDashboardTrabajador(Long trabajadorId) {
        EstadisticasResponseDTO estadisticas = obtenerEstadisticasTrabajador(trabajadorId);
        List<SolicitudResponseDTO> historial = obtenerHistorialTrabajador(trabajadorId);

        // Limitar a los ultimos 5 para el dashboard
        List<SolicitudResponseDTO> historialReciente = historial.stream()
                .limit(5)
                .collect(Collectors.toList());

        return DashboardResumenDTO.builder()
                .estadisticas(estadisticas)
                .historialReciente(historialReciente)
                .build();
    }
}
