package com.oficioYa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.mapper.PerfilContratanteMapper;
import com.oficioYa.mapper.PerfilTrabajadorMapper;
import com.oficioYa.mapper.UsuarioDTOMapper;
import com.oficioYa.model.domain.PerfilContratante;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.model.domain.Usuario;
import com.oficioYa.model.dto.request.EditarPerfilContratanteRequestDTO;
import com.oficioYa.model.dto.request.EditarPerfilTrabajadorRequestDTO;
import com.oficioYa.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioYa.model.dto.response.PerfilContratanteResponseDTO;
import com.oficioYa.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioYa.model.dto.response.UsuarioResponseDTO;
import com.oficioYa.persistence.entity.RolUsuario;
import com.oficioYa.service.IUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(UsuarioController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.oficioYa.security.jwt.JwtService jwtService;

    @org.springframework.beans.factory.annotation.Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private IUsuarioService usuarioService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private UsuarioDTOMapper dtoMapper;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private PerfilTrabajadorMapper perfilTrabajadorMapper;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private PerfilContratanteMapper perfilContratanteMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("POST /registro - exitoso retorna 201 Created")
    void registrarUsuario_exitoso() throws Exception {
        UsuarioRegistroRequestDTO request = new UsuarioRegistroRequestDTO(
                "carlos@test.com", "3001234567", "Password123!", "Carlos", "Gomez", RolUsuario.CONTRATANTE
        );
        Usuario dominio = Usuario.builder().id(1L).correo("carlos@test.com").build();
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                1L, "carlos@test.com", "3001234567", "Carlos", "Gomez",
                RolUsuario.CONTRATANTE, true, false, java.time.LocalDateTime.now(), true
        );

        when(dtoMapper.toDomain(any())).thenReturn(dominio);
        when(usuarioService.registrarUsuario(any())).thenReturn(dominio);
        when(dtoMapper.toResponseDTO(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.correo").value("carlos@test.com"));
    }

    @Test
    @DisplayName("PATCH /{correo}/verificar - exitoso retorna 200 OK")
    void verificarCorreo_exitoso() throws Exception {
        doNothing().when(usuarioService).verificarCorreo("test@test.com");

        mockMvc.perform(patch("/api/v1/usuarios/test@test.com/verificar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Correo verificado exitosamente"));
    }

    @Test
    @DisplayName("GET /{id}/perfil-trabajador - exitoso retorna 200 OK")
    void obtenerPerfilTrabajador_exitoso() throws Exception {
        PerfilTrabajador perfil = new PerfilTrabajador();
        perfil.setId(5L);
        PerfilTrabajadorResponseDTO responseDTO = new PerfilTrabajadorResponseDTO();
        responseDTO.setId(5L);
        responseDTO.setZonaCobertura("Norte");

        when(usuarioService.obtenerPerfilTrabajador(1L)).thenReturn(perfil);
        when(perfilTrabajadorMapper.toResponse(perfil)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/usuarios/1/perfil-trabajador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.zonaCobertura").value("Norte"));
    }

    @Test
    @DisplayName("GET /{id}/perfil-trabajador - usuario no encontrado retorna 404")
    void obtenerPerfilTrabajador_noEncontrado() throws Exception {
        when(usuarioService.obtenerPerfilTrabajador(99L))
                .thenThrow(new UsuarioNoEncontradoException("Usuario no encontrado"));

        mockMvc.perform(get("/api/v1/usuarios/99/perfil-trabajador"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id}/perfil-contratante - exitoso retorna 200 OK")
    void obtenerPerfilContratante_exitoso() throws Exception {
        PerfilContratante perfil = new PerfilContratante();
        perfil.setId(7L);
        PerfilContratanteResponseDTO responseDTO = new PerfilContratanteResponseDTO();
        responseDTO.setId(7L);
        responseDTO.setCalificacionPromedio(4.8);

        when(usuarioService.obtenerPerfilContratante(2L)).thenReturn(perfil);
        when(perfilContratanteMapper.toResponse(perfil)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/usuarios/2/perfil-contratante"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.calificacionPromedio").value(4.8));
    }

    @Test
    @DisplayName("PUT /{id}/perfil-trabajador - exitoso retorna 200 OK")
    void editarPerfilTrabajador_exitoso() throws Exception {
        EditarPerfilTrabajadorRequestDTO request = new EditarPerfilTrabajadorRequestDTO("3009876543", "Suba");

        doNothing().when(usuarioService).editarPerfilTrabajador(1L, "3009876543", "Suba");

        mockMvc.perform(put("/api/v1/usuarios/1/perfil-trabajador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Perfil de trabajador actualizado exitosamente"));
    }

    @Test
    @DisplayName("PUT /{id}/perfil-contratante - exitoso retorna 200 OK")
    void editarPerfilContratante_exitoso() throws Exception {
        EditarPerfilContratanteRequestDTO request = new EditarPerfilContratanteRequestDTO("3101112233", "Busco reparaciones");

        doNothing().when(usuarioService).editarPerfilContratante(1L, "3101112233", "Busco reparaciones");

        mockMvc.perform(put("/api/v1/usuarios/1/perfil-contratante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Perfil de contratante actualizado exitosamente"));
    }

    @Test
    @DisplayName("DELETE /{id} - exitoso retorna 200 OK")
    void eliminarCuenta_exitoso() throws Exception {
        doNothing().when(usuarioService).eliminarCuenta(1L);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cuenta eliminada exitosamente"));
    }

    @Test
    @DisplayName("POST /{id}/foto - exitoso retorna 200 OK")
    void actualizarFotoPerfil_exitoso() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "fake-image-bytes".getBytes()
        );

        doNothing().when(usuarioService).actualizarFotoPerfil(eq(1L), any());

        mockMvc.perform(multipart("/api/v1/usuarios/1/foto").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Foto de perfil actualizada exitosamente"));
    }
}
