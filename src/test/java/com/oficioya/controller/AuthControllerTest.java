package com.oficioYa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficioYa.dto.AuthRequest;
import com.oficioYa.dto.SwitchRoleRequest;
import com.oficioYa.security.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe autenticar y retornar token JWT cuando las credenciales son válidas")
    void login_ConCredencialesValidas_DebeRetornarToken() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("testuser@oficioya.com", "password123");
        String expectedToken = "mocked-jwt-token-login";

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
               .thenReturn(null);
        Mockito.when(jwtService.generateToken(any(), any())).thenReturn(expectedToken);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any(), any());
    }

    @Test
    @DisplayName("Debe fallar autenticación cuando las credenciales son incorrectas")
    void login_ConCredencialesInvalidas_DebeRetornarUnauthorized() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("usuario@invalido.com", "clave_erronea");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
               .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Debe alternar rol a TRABAJADOR y retornar nuevo token JWT")
    void switchRole_ConRolValido_DebeRetornarNuevoToken() throws Exception {
        // Arrange
        SwitchRoleRequest request = new SwitchRoleRequest("TRABAJADOR");
        String expectedNewToken = "mocked-jwt-token-trabajador";

        Mockito.when(jwtService.extractUsername("old-mocked-jwt-token")).thenReturn("testuser@oficioya.com");
        Mockito.when(jwtService.generateToken(any(), any())).thenReturn(expectedNewToken);

        // Act & Assert
        mockMvc.perform(post("/auth/switch-role")
                .header("Authorization", "Bearer old-mocked-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedNewToken));

        // Assert
        verify(jwtService).extractUsername("old-mocked-jwt-token");
        verify(jwtService).generateToken(any(), any());
    }

    @Test
    @DisplayName("Debe alternar rol cuando el rol ya contiene prefijo ROLE_")
    void switchRole_ConPrefijoRole_DebeNormalizarYRetornarNuevoToken() throws Exception {
        // Arrange
        SwitchRoleRequest request = new SwitchRoleRequest("ROLE_CONTRATANTE");
        String expectedNewToken = "mocked-jwt-token-contratante";

        Mockito.when(jwtService.extractUsername("jwt-token-xyz")).thenReturn("admin@oficioya.com");
        Mockito.when(jwtService.generateToken(any(), any())).thenReturn(expectedNewToken);

        // Act & Assert
        mockMvc.perform(post("/auth/switch-role")
                .header("Authorization", "Bearer jwt-token-xyz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedNewToken));

        // Assert
        verify(jwtService).extractUsername("jwt-token-xyz");
        verify(jwtService).generateToken(any(), any());
    }
}
