package com.oficioya.service.impl;

import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.model.dto.request.ReporteRequestDTO;
import com.oficioya.model.dto.response.ReporteResponseDTO;
import com.oficioya.persistence.entity.EstadoReporte;
import com.oficioya.persistence.entity.ReporteEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.ReporteRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IModeracionService;
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
    public ReporteResponseDTO reportarUsuario(String correoReportador, ReporteRequestDTO request) {
        log.info("Creando reporte del usuario {} hacia el usuario {}", correoReportador, request.reportadoId());
        
        UsuarioEntity reportador = usuarioRepository.findByCorreo(correoReportador)
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
