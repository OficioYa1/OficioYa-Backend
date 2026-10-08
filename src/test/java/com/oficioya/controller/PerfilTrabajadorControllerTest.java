package com.oficioya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.*;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.service.IPerfilTrabajadorService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PerfilTrabajadorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IPerfilTrabajadorService perfilService;

    @Mock
    private PerfilTrabajadorMapper mapper;

    @InjectMocks
    private PerfilTrabajadorController controller;

    private ObjectMapper objectMapper;
    private PerfilTrabajador perfilDomain;
    private PerfilTrabajadorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        perfilDomain = new PerfilTrabajador();
        perfilDomain.setId(1L);

        responseDTO = new PerfilTrabajadorResponseDTO();
        responseDTO.setId(1L);
    }

    @Test
    @DisplayName("POST /api/v1/perfiles-trabajador - 201 Created")
    void crearPerfil_exitoso() throws Exception {
        PerfilTrabajadorCreacionRequestDTO req = new PerfilTrabajadorCreacionRequestDTO();
        req.setUsuarioId(1L);
        req.setDescripcion("Plomero experto");

        when(mapper.toDomain(any())).thenReturn(perfilDomain);
        when(perfilService.crearPerfil(any())).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/perfiles-trabajador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /{id}/zona-cobertura - 200 OK")
    void actualizarZonaCobertura_exitoso() throws Exception {
        ZonaCoberturaRequestDTO req = new ZonaCoberturaRequestDTO();
        req.setZonaCobertura("Chapinero");

        when(perfilService.actualizarZonaCobertura(1L, "Chapinero")).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/perfiles-trabajador/1/zona-cobertura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /{id}/tarifa - 200 OK")
    void actualizarTarifa_exitoso() throws Exception {
        TarifaRequestDTO req = new TarifaRequestDTO();
        req.setTarifaPorHora(new BigDecimal("35000.00"));

        when(perfilService.actualizarTarifa(eq(1L), any())).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/perfiles-trabajador/1/tarifa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /{id}/disponibilidad-semanal - 200 OK")
    void actualizarDisponibilidadSemanal_exitoso() throws Exception {
        DisponibilidadSemanalRequestDTO req = new DisponibilidadSemanalRequestDTO();
        req.setFranjas(Collections.emptyList());

        when(mapper.toFranjasDomain(any())).thenReturn(Collections.emptyList());
        when(perfilService.actualizarDisponibilidadSemanal(eq(1L), any())).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/perfiles-trabajador/1/disponibilidad-semanal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /{id}/metodos-pago - 200 OK")
    void actualizarMetodosPago_exitoso() throws Exception {
        MetodosPagoRequestDTO req = new MetodosPagoRequestDTO();
        req.setMetodosPago(java.util.Set.of(com.oficioya.model.domain.MetodoPago.EFECTIVO));

        when(perfilService.actualizarMetodosPago(eq(1L), any())).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/perfiles-trabajador/1/metodos-pago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /{id}/disponible-ahora/activar - 200 OK")
    void activarDisponibleAhora_exitoso() throws Exception {
        when(perfilService.activarDisponibleAhora(1L)).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/perfiles-trabajador/1/disponible-ahora/activar"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /{id}/disponible-ahora/desactivar - 200 OK")
    void desactivarDisponibleAhora_exitoso() throws Exception {
        when(perfilService.desactivarDisponibleAhora(1L)).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/perfiles-trabajador/1/disponible-ahora/desactivar"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /{id}/oficio-principal - 200 OK")
    void registrarOficioPrincipal_exitoso() throws Exception {
        OficioPrincipalRequestDTO req = new OficioPrincipalRequestDTO();
        req.setOficioId(2L);

        when(perfilService.registrarOficioPrincipal(1L, 2L)).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/perfiles-trabajador/1/oficio-principal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /{id}/oficios-secundarios - 200 OK")
    void registrarOficiosSecundarios_exitoso() throws Exception {
        OficiosSecundariosRequestDTO req = new OficiosSecundariosRequestDTO();
        req.setOficiosIds(List.of(3L, 4L));

        when(perfilService.registrarOficiosSecundarios(1L, List.of(3L, 4L))).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/perfiles-trabajador/1/oficios-secundarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /{id}/detalles-especificos - 200 OK")
    void actualizarDetallesEspecificos_exitoso() throws Exception {
        DetallesEspecificosRequestDTO req = new DetallesEspecificosRequestDTO();
        req.setDetallesEspecificos("Detalles");

        when(perfilService.actualizarDetallesEspecificos(1L, "Detalles")).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/perfiles-trabajador/1/detalles-especificos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /{id}/portafolio - 200 OK")
    void actualizarPortafolio_exitoso() throws Exception {
        PortafolioRequestDTO req = new PortafolioRequestDTO();
        req.setFotosPortafolio(List.of("http://foto1.jpg"));

        when(perfilService.actualizarPortafolio(1L, List.of("http://foto1.jpg"))).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/perfiles-trabajador/1/portafolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /{id} - 200 OK")
    void obtenerPerfilPorId_exitoso() throws Exception {
        when(perfilService.obtenerPerfilPorId(1L)).thenReturn(perfilDomain);
        when(mapper.toResponse(perfilDomain)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/perfiles-trabajador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
