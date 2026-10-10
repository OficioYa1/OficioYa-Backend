package com.oficioya.service.impl;

import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.persistence.entity.EstadoSolicitud;
import com.oficioya.model.dto.request.ReporteResenaRequestDTO;
import com.oficioya.model.dto.request.ResenaRequestDTO;
import com.oficioya.model.dto.response.ReputacionPorOficioDTO;
import com.oficioya.model.dto.response.ResenaResponseDTO;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.persistence.entity.ResenaEntity;
import com.oficioya.persistence.entity.SolicitudEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.OficioRepository;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.ResenaRepository;
import com.oficioya.repository.SolicitudRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.IReputacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReputacionServiceImpl implements IReputacionService {

    private final ResenaRepository resenaRepository;
    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilTrabajadorRepository perfilRepository;

    @Override
    @Transactional
    public ResenaResponseDTO calificarTrabajador(Long solicitudId, Long contratanteId, ResenaRequestDTO request) {
        SolicitudEntity solicitud = validarSolicitudFinalizada(solicitudId);

        if (!solicitud.getContratante().getId().equals(contratanteId)) {
            throw new EstadoInvalidoException("Solo el contratante de la solicitud puede calificar al trabajador");
        }

        ResenaEntity resena = ResenaEntity.builder()
                .autor(solicitud.getContratante())
                .receptor(solicitud.getTrabajador())
                .solicitud(solicitud)
                .oficio(perfilRepository.findByUsuarioId(solicitud.getTrabajador().getId()).orElseThrow().getOficioPrincipal())
                .estrellas(request.getEstrellas())
                .comentario(request.getComentario())
                .build();

        resena = resenaRepository.save(resena);
        recalcularPromedioTrabajador(solicitud.getTrabajador().getId());

        return mapToResponseDTO(resena);
    }

    @Override
    @Transactional
    public ResenaResponseDTO calificarContratante(Long solicitudId, Long trabajadorId, ResenaRequestDTO request) {
        SolicitudEntity solicitud = validarSolicitudFinalizada(solicitudId);

        if (!solicitud.getTrabajador().getId().equals(trabajadorId)) {
            throw new EstadoInvalidoException("Solo el trabajador de la solicitud puede calificar al contratante");
        }

        ResenaEntity resena = ResenaEntity.builder()
                .autor(solicitud.getTrabajador())
                .receptor(solicitud.getContratante())
                .solicitud(solicitud)
                .oficio(perfilRepository.findByUsuarioId(solicitud.getTrabajador().getId()).orElseThrow().getOficioPrincipal())
                .estrellas(request.getEstrellas())
                .comentario(request.getComentario())
                .build();

        resena = resenaRepository.save(resena);
        recalcularPromedioContratante(solicitud.getContratante().getId());

        return mapToResponseDTO(resena);
    }

    @Override
    @Transactional
    public void reportarResena(Long resenaId, ReporteResenaRequestDTO request) {
        ResenaEntity resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reseña no encontrada"));
        
        if (resena.isReportada()) {
            throw new EstadoInvalidoException("La reseña ya había sido reportada");
        }

        resena.setReportada(true);
        // Opcional: guardar el motivo del reporte en otra tabla o log.
        log.info("Reseña {} reportada con motivo: {}", resenaId, request.getMotivo());
        resenaRepository.save(resena);

        // Al reportar una reseña, se excluye del promedio, así que recalculamos.
        recalcularPromedios(resena.getReceptor().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ReputacionPorOficioDTO obtenerReputacionPorOficio(Long trabajadorId, Long oficioId) {
        Double promedio = resenaRepository.calcularPromedioPorReceptorYOficio(trabajadorId, oficioId).orElse(0.0);
        long total = resenaRepository.countByReceptorIdAndReportadaFalse(trabajadorId); // Opcionalmente filtrar por oficio

        return ReputacionPorOficioDTO.builder()
                .trabajadorId(trabajadorId)
                .oficioId(oficioId)
                .oficioNombre("Oficio " + oficioId) // Idealmente mapeado
                .calificacionPromedio(Math.round(promedio * 10.0) / 10.0)
                .totalResenas(total)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> obtenerResenasDeUsuario(Long usuarioId) {
        return resenaRepository.findByReceptorId(usuarioId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private SolicitudEntity validarSolicitudFinalizada(Long solicitudId) {
        SolicitudEntity solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));

        if (solicitud.getEstado() != EstadoSolicitud.COMPLETADA) {
            throw new EstadoInvalidoException("Solo se pueden calificar solicitudes finalizadas");
        }
        return solicitud;
    }

    private void recalcularPromedios(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        if ("TRABAJADOR".equals(usuario.getRol().name())) {
            recalcularPromedioTrabajador(usuarioId);
        } else {
            recalcularPromedioContratante(usuarioId);
        }
    }

    private void recalcularPromedioTrabajador(Long trabajadorId) {
        Double nuevoPromedio = resenaRepository.calcularPromedioPorReceptor(trabajadorId).orElse(0.0);
        nuevoPromedio = Math.round(nuevoPromedio * 10.0) / 10.0;

        PerfilTrabajadorEntity perfil = perfilRepository.findByUsuarioId(trabajadorId).orElseThrow();
        perfil.setCalificacionPromedio(nuevoPromedio);

        // RF-39: Pausar trabajador automático si el promedio cae mucho y tiene al menos 3 reseñas
        long totalResenas = resenaRepository.countByReceptorIdAndReportadaFalse(trabajadorId);
        if (totalResenas >= 3 && nuevoPromedio < 3.0) {
            perfil.setDisponibleAhora(false);
            log.warn("Trabajador {} pausado automáticamente por baja reputación ({})", trabajadorId, nuevoPromedio);
        }

        perfilRepository.save(perfil);
    }

    private void recalcularPromedioContratante(Long contratanteId) {
        Double nuevoPromedio = resenaRepository.calcularPromedioPorReceptor(contratanteId).orElse(0.0);
        nuevoPromedio = Math.round(nuevoPromedio * 10.0) / 10.0;

        UsuarioEntity contratante = usuarioRepository.findById(contratanteId).orElseThrow();
        contratante.setCalificacionPromedio(nuevoPromedio);
        usuarioRepository.save(contratante);
    }

    private ResenaResponseDTO mapToResponseDTO(ResenaEntity entity) {
        return ResenaResponseDTO.builder()
                .id(entity.getId())
                .autorId(entity.getAutor().getId())
                .autorNombre(entity.getAutor().getNombre() + " " + entity.getAutor().getApellido())
                .autorFoto(entity.getAutor().getFotoPerfil())
                .receptorId(entity.getReceptor().getId())
                .receptorNombre(entity.getReceptor().getNombre() + " " + entity.getReceptor().getApellido())
                .oficioId(entity.getOficio().getId())
                .oficioNombre(entity.getOficio().getNombre())
                .estrellas(entity.getEstrellas())
                .comentario(entity.getComentario())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}
