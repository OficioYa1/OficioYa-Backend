package com.oficioya.controller;

import com.oficioya.mapper.BusquedaMapper;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.service.IBusquedaTrabajadorService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(BusquedaController.class)
class BusquedaControllerTest {

    @org.springframework.beans.factory.annotation.Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private IBusquedaTrabajadorService busquedaService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private PerfilTrabajadorMapper mapper;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private BusquedaMapper busquedaMapper;

    @Test
    @DisplayName("GET /api/v1/busqueda/trabajadores - exitoso retorna lista 200 OK")
    void buscarTrabajadores_exitoso() throws Exception {
        PerfilTrabajador trabajador = new PerfilTrabajador();
        trabajador.setId(1L);

        PerfilTrabajadorResponseDTO responseDTO = new PerfilTrabajadorResponseDTO();
        responseDTO.setId(1L);

        when(busquedaMapper.toCriterios(any())).thenReturn(null);
        when(busquedaService.buscar(any())).thenReturn(List.of(trabajador));
        when(mapper.toResponse(trabajador)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/busqueda/trabajadores")
                        .param("necesidad", "plomero"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
