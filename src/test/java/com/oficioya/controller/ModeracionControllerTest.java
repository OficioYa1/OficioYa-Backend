package com.oficioya.controller;

import com.oficioya.model.dto.request.ReporteRequestDTO;
import com.oficioya.model.dto.response.ReporteResponseDTO;
import com.oficioya.service.IModeracionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeracionControllerTest {

    @Mock
    private IModeracionService moderacionService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ModeracionController moderacionController;

    private ReporteRequestDTO requestDTO;
    private ReporteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ReporteRequestDTO(2L, "Motivo de prueba", "Comportamiento inadecuado");
        responseDTO = new ReporteResponseDTO(1L, 1L, 2L, "Motivo de prueba", "Comportamiento inadecuado", "PENDIENTE", java.time.LocalDateTime.now());
    }

    @Test
    void reportarUsuario() {
        when(principal.getName()).thenReturn("reportador@mail.com");
        when(moderacionService.reportarUsuario("reportador@mail.com", requestDTO)).thenReturn(responseDTO);

        ResponseEntity<ReporteResponseDTO> response = moderacionController.reportarUsuario(principal, requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(moderacionService).reportarUsuario("reportador@mail.com", requestDTO);
    }

    @Test
    void consultarEstadoReporte() {
        when(moderacionService.consultarEstadoReporte(1L)).thenReturn(responseDTO);

        ResponseEntity<ReporteResponseDTO> response = moderacionController.consultarEstadoReporte(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }
}
