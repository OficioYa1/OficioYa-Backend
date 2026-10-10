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
import com.oficioYa.service.IModeracionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModeracionServiceImpl implements IModeracionService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ReporteResponseDTO reportarUsuario(Long reportadorId, ReporteRequestDTO request) {
        log.info("Creando reporte del usuario {} hacia el usuario {}", reportadorId, request.reportadoId());
        
        UsuarioEntity reportador = usuarioRepository.findById(reportadorId)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Reportador no encontrado"));
            
        UsuarioEntity reportado = usuarioRepository.findById(request.reportadoId())
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario reportado no encontrado"));
            
        ReporteEntity reporte = ReporteEntity.builder()
            .reportador(reportador)
            .reportado(reportado)
            .motivo(request.motivo())
            .evidenciaUrl(request.evidenciaUrl())
            .estado(EstadoReporte.PENDIENTE)
            .fechaCreacion(LocalDateTime.now())
            .build();
            
        ReporteEntity guardado = reporteRepository.save(reporte);
        return toDTO(guardado);
    }

    @Override
    public ReporteResponseDTO consultarEstadoReporte(Long reporteId) {
        ReporteEntity reporte = reporteRepository.findById(reporteId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Reporte no encontrado"));
        return toDTO(reporte);
    }

    @Override
    public List<ReporteResponseDTO> obtenerTodosLosReportes() {
        return reporteRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void pausarUsuario(Long usuarioId) {
        log.info("Pausando cuenta del usuario {}", usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        
        usuario.setActivo(false); // Pausa temporal
        usuarioRepository.save(usuario);
    }

    private ReporteResponseDTO toDTO(ReporteEntity entity) {
        return new ReporteResponseDTO(
            entity.getId(),
            entity.getReportador().getId(),
            entity.getReportado().getId(),
            entity.getMotivo(),
            entity.getEvidenciaUrl(),
            entity.getEstado().name(),
            entity.getFechaCreacion()
        );
    }
}
