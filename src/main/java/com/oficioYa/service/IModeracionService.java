package com.oficioya.service;

import com.oficioya.model.dto.request.ReporteRequestDTO;
import com.oficioya.model.dto.response.ReporteResponseDTO;

import java.util.List;

public interface IModeracionService {
    ReporteResponseDTO reportarUsuario(Long reportadorId, ReporteRequestDTO request);
    ReporteResponseDTO consultarEstadoReporte(Long reporteId);
    List<ReporteResponseDTO> obtenerTodosLosReportes();
    void pausarUsuario(Long usuarioId);
}
