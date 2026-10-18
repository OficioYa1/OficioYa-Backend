package com.oficioya.security;

import com.oficioya.service.IModeracionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IModeracionService moderacionService;

    @Test
    @DisplayName("Endpoint protegido sin token de autenticación debe rechazar con 401 o 403")
    void sinToken_DebeRechazarAcceso() throws Exception {
        mockMvc.perform(get("/api/v1/moderacion/reportes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "contratante@oficioya.com", roles = {"CONTRATANTE"})
    @DisplayName("Endpoint restringido a ADMIN invocado por CONTRATANTE debe retornar 403 Forbidden")
    void rolIncorrecto_DebeRetornarForbidden403() throws Exception {
        mockMvc.perform(get("/api/v1/moderacion/reportes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@oficioya.com", roles = {"ADMIN"})
    @DisplayName("Endpoint restringido a ADMIN invocado por ADMIN debe retornar 200 OK")
    void rolCorrecto_DebeRetornarOk200() throws Exception {
        when(moderacionService.obtenerTodosLosReportes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/moderacion/reportes"))
                .andExpect(status().isOk());
    }
}
