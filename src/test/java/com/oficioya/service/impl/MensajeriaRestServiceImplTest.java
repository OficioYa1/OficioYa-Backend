package com.oficioya.service.impl;

import com.oficioya.model.dto.request.MensajeChatRequestDTO;
import com.oficioya.model.dto.response.MensajeChatResponseDTO;
import com.oficioya.persistence.document.MensajeDocument;
import com.oficioya.repository.MensajeMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensajeriaRestServiceImplTest {

    @Mock
    private MensajeMongoRepository mensajeRepository;

    @InjectMocks
    private MensajeriaRestServiceImpl mensajeriaService;

    private MensajeDocument doc;
    private MensajeChatRequestDTO req;

    @BeforeEach
    void setUp() {
        doc = new MensajeDocument();
        doc.setId("mongo-123");
        doc.setSolicitudId(1L);
        doc.setRemitenteId(2L);
        doc.setDestinatarioId(3L);
        doc.setContenido("Hola!");
        doc.setFechaEnvio(LocalDateTime.now());
        doc.setLeido(false);

        req = new MensajeChatRequestDTO();
        req.setRemitenteId(2L);
        req.setDestinatarioId(3L);
        req.setContenido("Hola!");
    }

    @Test
    void enviarMensaje_Success() {
        when(mensajeRepository.save(any(MensajeDocument.class))).thenReturn(doc);

        MensajeChatResponseDTO res = mensajeriaService.enviarMensaje(1L, req);

        assertNotNull(res);
        assertEquals("mongo-123", res.getId());
        assertEquals("Hola!", res.getContenido());
        verify(mensajeRepository, times(1)).save(any(MensajeDocument.class));
    }

    @Test
    void obtenerMensajesPorSolicitud_Success() {
        when(mensajeRepository.findBySolicitudIdOrderByFechaEnvioAsc(1L)).thenReturn(Arrays.asList(doc));

        List<MensajeChatResponseDTO> res = mensajeriaService.obtenerMensajesPorSolicitud(1L);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("mongo-123", res.get(0).getId());
    }

    @Test
    void marcarMensajesComoLeidos_Success() {
        when(mensajeRepository.findBySolicitudIdAndDestinatarioIdAndLeidoFalse(1L, 3L))
                .thenReturn(Arrays.asList(doc));

        mensajeriaService.marcarMensajesComoLeidos(1L, 3L);

        assertTrue(doc.isLeido());
        verify(mensajeRepository, times(1)).saveAll(anyList());
    }

    @Test
    void marcarMensajesComoLeidos_EmptyList() {
        when(mensajeRepository.findBySolicitudIdAndDestinatarioIdAndLeidoFalse(1L, 3L))
                .thenReturn(Arrays.asList());

        mensajeriaService.marcarMensajesComoLeidos(1L, 3L);

        verify(mensajeRepository, never()).saveAll(anyList());
    }
}
