package com.oficioya.controller;

import com.oficioya.model.dto.request.ReporteResenaRequestDTO;
import com.oficioya.model.dto.request.ResenaRequestDTO;
import com.oficioya.model.dto.response.ReputacionPorOficioDTO;
import com.oficioya.model.dto.response.ResenaResponseDTO;
import com.oficioya.service.IReputacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ReputacionControllerTest {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.oficioya.security.jwt.JwtService jwtService;

    @Mock
    private IReputacionService reputacionService;

    @InjectMocks
    private ReputacionController controller;

    private ResenaRequestDTO resenaRequest;
    private ReporteResenaRequestDTO reporteRequest;
    private ResenaResponseDTO resenaResponse;

    @BeforeEach
    void setUp() {
        resenaRequest = new ResenaRequestDTO();
        resenaRequest.setEstrellas(5);
        resenaRequest.setComentario("Excelente trabajo");

        reporteRequest = new ReporteResenaRequestDTO();
        reporteRequest.setMotivo("Lenguaje inapropiado");

        resenaResponse = ResenaResponseDTO.builder()
                .id(1L)
                .autorId(2L)
                .receptorId(3L)
                .estrellas(5)
                .comentario("Excelente trabajo")
                .build();
    }

    @Test
    void calificarTrabajador_retornaCreated() {
        when(reputacionService.calificarTrabajador(eq(1L), eq(2L), any(ResenaRequestDTO.class)))
                .thenReturn(resenaResponse);

        ResponseEntity<ResenaResponseDTO> res = controller.calificarTrabajador(1L, 2L, resenaRequest);

        assertEquals(201, res.getStatusCode().value());
        assertEquals(1L, res.getBody().getId());
    }

    @Test
    void calificarContratante_retornaCreated() {
        when(reputacionService.calificarContratante(eq(1L), eq(3L), any(ResenaRequestDTO.class)))
                .thenReturn(resenaResponse);

        ResponseEntity<ResenaResponseDTO> res = controller.calificarContratante(1L, 3L, resenaRequest);

        assertEquals(201, res.getStatusCode().value());
        assertEquals(1L, res.getBody().getId());
    }

    @Test
    void reportarResena_retornaNoContent() {
        ResponseEntity<Void> res = controller.reportarResena(1L, reporteRequest);
        assertEquals(204, res.getStatusCode().value());
        verify(reputacionService).reportarResena(eq(1L), any(ReporteResenaRequestDTO.class));
    }

    @Test
    void obtenerReputacionPorOficio_retornaOk() {
        ReputacionPorOficioDTO dto = ReputacionPorOficioDTO.builder()
                .trabajadorId(3L)
                .oficioId(1L)
                .calificacionPromedio(4.5)
                .totalResenas(10L)
                .build();

        when(reputacionService.obtenerReputacionPorOficio(3L, 1L)).thenReturn(dto);

        ResponseEntity<ReputacionPorOficioDTO> res = controller.obtenerReputacionPorOficio(3L, 1L);

        assertEquals(200, res.getStatusCode().value());
        assertEquals(4.5, res.getBody().getCalificacionPromedio());
    }

    @Test
    void obtenerResenasDeUsuario_retornaOk() {
        when(reputacionService.obtenerResenasDeUsuario(3L)).thenReturn(List.of(resenaResponse));

        ResponseEntity<List<ResenaResponseDTO>> res = controller.obtenerResenasDeUsuario(3L);

        assertEquals(200, res.getStatusCode().value());
        assertEquals(1, res.getBody().size());
    }
}
