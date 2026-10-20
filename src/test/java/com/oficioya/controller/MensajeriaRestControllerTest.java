package com.oficioya.controller;

import com.oficioya.model.dto.request.MensajeChatRequestDTO;
import com.oficioya.model.dto.response.MensajeChatResponseDTO;
import com.oficioya.service.IMensajeriaRestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class MensajeriaRestControllerTest {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.oficioya.security.jwt.JwtService jwtService;

    @Mock
    private IMensajeriaRestService mensajeriaService;

    @InjectMocks
    private MensajeriaRestController controller;

    @Test
    void enviarMensaje_Success() {
        // Arrange
        MensajeChatRequestDTO req = new MensajeChatRequestDTO();
        when(mensajeriaService.enviarMensaje(1L, req)).thenReturn(MensajeChatResponseDTO.builder().id("123").build());
        
        // Act
        ResponseEntity<MensajeChatResponseDTO> res = controller.enviarMensaje(1L, req);
        
        // Assert
        assertEquals(200, res.getStatusCode().value());
        assertEquals("123", res.getBody().getId());
    }

    @Test
    void obtenerMensajesPorSolicitud_Success() {
        // Arrange
        when(mensajeriaService.obtenerMensajesPorSolicitud(1L))
                .thenReturn(Arrays.asList(MensajeChatResponseDTO.builder().id("123").build()));
        
        // Act
        ResponseEntity<List<MensajeChatResponseDTO>> res = controller.obtenerMensajesPorSolicitud(1L);
        
        // Assert
        assertEquals(200, res.getStatusCode().value());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void marcarMensajesComoLeidos_Success() {
        // Act
        ResponseEntity<Void> res = controller.marcarMensajesComoLeidos(1L, 2L);
        
        // Assert
        assertEquals(204, res.getStatusCode().value());
        verify(mensajeriaService, times(1)).marcarMensajesComoLeidos(1L, 2L);
    }
}
