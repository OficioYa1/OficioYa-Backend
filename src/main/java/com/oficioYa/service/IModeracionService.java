package com.oficioYa.service;

import com.oficioYa.model.dto.request.ReporteRequestDTO;
import com.oficioYa.model.dto.response.ReporteResponseDTO;

import java.util.List;

public interface IModeracionService {
    ReporteResponseDTO reportarUsuario(Long reportadorId, ReporteRequestDTO request);
    ReporteResponseDTO consultarEstadoReporte(Long reporteId);
    List<ReporteResponseDTO> obtenerTodosLosReportes();
    void pausarUsuario(Long usuarioId);
}
