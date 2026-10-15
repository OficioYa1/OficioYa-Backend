package com.oficioya.controller;

import com.oficioya.model.dto.response.MensajeResponseDTO;
import com.oficioya.service.IReferidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferidoControllerTest {

    @Mock
    private IReferidoService referidoService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ReferidoController referidoController;

    @Test
    void registrarReferido() {
        when(principal.getName()).thenReturn("referente@mail.com");

        ResponseEntity<MensajeResponseDTO> response = referidoController.registrarReferido(principal, "amigo@mail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(referidoService).registrarReferido("referente@mail.com", "amigo@mail.com");
    }
}
