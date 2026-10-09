package com.oficioya.service.impl;

import com.oficioya.model.dto.request.MensajeChatRequestDTO;
import com.oficioya.model.dto.response.MensajeChatResponseDTO;
import com.oficioya.persistence.document.MensajeDocument;
import com.oficioya.repository.MensajeMongoRepository;
import com.oficioya.service.IMensajeriaRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MensajeriaRestServiceImpl implements IMensajeriaRestService {

    private final MensajeMongoRepository mensajeRepository;

    private MensajeChatResponseDTO mapearADTO(MensajeDocument doc) {
        return MensajeChatResponseDTO.builder()
                .id(doc.getId())
                .solicitudId(doc.getSolicitudId())
                .remitenteId(doc.getRemitenteId())
                .destinatarioId(doc.getDestinatarioId())
                .contenido(doc.getContenido())
                .fechaEnvio(doc.getFechaEnvio())
                .leido(doc.isLeido())
                .build();
    }

    @Override
    public MensajeChatResponseDTO enviarMensaje(Long solicitudId, MensajeChatRequestDTO request) {
        log.info("Enviando mensaje de {} a {} para solicitud {}", request.getRemitenteId(), request.getDestinatarioId(), solicitudId);
        MensajeDocument doc = MensajeDocument.builder()
                .solicitudId(solicitudId)
                .remitenteId(request.getRemitenteId())
                .destinatarioId(request.getDestinatarioId())
                .contenido(request.getContenido())
                .fechaEnvio(LocalDateTime.now())
                .leido(false)
                .build();

        MensajeDocument guardado = mensajeRepository.save(doc);
        return mapearADTO(guardado);
    }

    @Override
    public List<MensajeChatResponseDTO> obtenerMensajesPorSolicitud(Long solicitudId) {
        log.info("Obteniendo mensajes para solicitud {}", solicitudId);
        return mensajeRepository.findBySolicitudIdOrderByFechaEnvioAsc(solicitudId)
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    public void marcarMensajesComoLeidos(Long solicitudId, Long destinatarioId) {
        log.info("Marcando mensajes como leidos para solicitud {} y destinatario {}", solicitudId, destinatarioId);
        List<MensajeDocument> noLeidos = mensajeRepository.findBySolicitudIdAndDestinatarioIdAndLeidoFalse(solicitudId, destinatarioId);
        for (MensajeDocument msj : noLeidos) {
            msj.setLeido(true);
        }
        if (!noLeidos.isEmpty()) {
            mensajeRepository.saveAll(noLeidos);
        }
    }
}
