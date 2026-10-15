package com.oficioya.controller;

import com.oficioya.model.dto.response.OficioResponseDTO;
import com.oficioya.service.IOficioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(OficioController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class OficioControllerTest {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.oficioya.security.jwt.JwtService jwtService;

    @org.springframework.beans.factory.annotation.Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private IOficioService oficioService;

    @Test
    @DisplayName("GET /api/v1/oficios - exitoso retorna lista de oficios 200 OK")
    void listarOficiosActivos_exitoso() throws Exception {
        OficioResponseDTO dto = OficioResponseDTO.builder()
                .id(1L)
                .nombre("Plomería")
                .descripcion("Servicios de plomería")
                .activo(true)
                .build();

        when(oficioService.listarOficiosActivos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/oficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Plomería"));
    }
}
