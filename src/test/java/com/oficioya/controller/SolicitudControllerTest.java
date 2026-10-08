package com.oficioya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficioya.mapper.SolicitudMapper;
import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.dto.request.SolicitudCreacionDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import com.oficioya.persistence.entity.EstadoSolicitud;
import com.oficioya.service.ISolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SolicitudControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ISolicitudService solicitudService;

    @Mock
    private SolicitudMapper mapper;

    @InjectMocks
    private SolicitudController solicitudController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(solicitudController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/v1/solicitudes - crear solicitud retorna 201 Created")
    void crearSolicitud_exitoso() throws Exception {
        SolicitudCreacionDTO dto = new SolicitudCreacionDTO();
        dto.setContratanteId(1L);
        dto.setDescripcion("Arreglar tuberia");
        dto.setZonaServicio("Norte");

        Solicitud dominio = new Solicitud();
        dominio.setId(10L);
        dominio.setEstadoEnum(EstadoSolicitud.CREADA);

        SolicitudResponseDTO responseDTO = new SolicitudResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setEstado("CREADA");

        when(mapper.toDomain(any())).thenReturn(dominio);
        when(solicitudService.crearSolicitud(any(), eq(1L))).thenReturn(dominio);
        when(mapper.toResponse(dominio)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.estado").value("CREADA"));
    }

    @Test
    @DisplayName("POST /{id}/enviar - exitoso retorna 200 OK")
    void enviarSolicitud_exitoso() throws Exception {
        doNothing().when(solicitudService).enviarSolicitud(10L, 20L);

        mockMvc.perform(post("/api/v1/solicitudes/10/enviar")
                        .param("trabajadorId", "20"))
                .andExpect(status().isOk());

        verify(solicitudService).enviarSolicitud(10L, 20L);
    }

    @Test
    @DisplayName("PATCH /{id}/aceptar - exitoso retorna 200 OK")
    void aceptarSolicitud_exitoso() throws Exception {
        doNothing().when(solicitudService).aceptarSolicitud(10L);

        mockMvc.perform(patch("/api/v1/solicitudes/10/aceptar"))
                .andExpect(status().isOk());

        verify(solicitudService).aceptarSolicitud(10L);
    }

    @Test
    @DisplayName("PATCH /{id}/rechazar - exitoso retorna 200 OK")
    void rechazarSolicitud_exitoso() throws Exception {
        doNothing().when(solicitudService).rechazarSolicitud(10L);

        mockMvc.perform(patch("/api/v1/solicitudes/10/rechazar"))
                .andExpect(status().isOk());

        verify(solicitudService).rechazarSolicitud(10L);
    }

    @Test
    @DisplayName("PATCH /{id}/iniciar - exitoso retorna 200 OK")
    void iniciarSolicitud_exitoso() throws Exception {
        doNothing().when(solicitudService).iniciarSolicitud(10L);

        mockMvc.perform(patch("/api/v1/solicitudes/10/iniciar"))
                .andExpect(status().isOk());

        verify(solicitudService).iniciarSolicitud(10L);
    }

    @Test
    @DisplayName("PATCH /{id}/completar - exitoso retorna 200 OK")
    void completarSolicitud_exitoso() throws Exception {
        doNothing().when(solicitudService).completarSolicitud(10L);

        mockMvc.perform(patch("/api/v1/solicitudes/10/completar"))
                .andExpect(status().isOk());

        verify(solicitudService).completarSolicitud(10L);
    }

    @Test
    @DisplayName("PATCH /{id}/cancelar - exitoso retorna 200 OK")
    void cancelarSolicitud_exitoso() throws Exception {
        doNothing().when(solicitudService).cancelarSolicitud(10L, "Motivo cancelacion");

        mockMvc.perform(patch("/api/v1/solicitudes/10/cancelar")
                        .param("motivo", "Motivo cancelacion"))
                .andExpect(status().isOk());

        verify(solicitudService).cancelarSolicitud(10L, "Motivo cancelacion");
    }
}
