package com.oficioya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficioya.dto.AuthRequest;
import com.oficioya.dto.SwitchRoleRequest;
import com.oficioya.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for pure controller test
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.when(jwtService.generateToken(any(), any())).thenReturn("mocked-jwt-token");
        Mockito.when(jwtService.extractUsername(anyString())).thenReturn("testuser@oficioya.com");
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthRequest request = new AuthRequest("testuser@oficioya.com", "password123");

        // Mock authentication success
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
               .thenReturn(null);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testSwitchRoleSuccess() throws Exception {
        SwitchRoleRequest request = new SwitchRoleRequest("TRABAJADOR");

        mockMvc.perform(post("/auth/switch-role")
                .header("Authorization", "Bearer old-mocked-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }
}
