package com.oficioya.controller;

import com.oficioya.model.domain.EstadoVerificacion;
import com.oficioya.service.IVerificacionIdentidadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class VerificacionIdentidadControllerTest {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.oficioya.security.jwt.JwtService jwtService;

    @Mock
    private IVerificacionIdentidadService verificacionService;

    @InjectMocks
    private VerificacionIdentidadController controller;

    @Test
    void subirDocumentosIdentidad_Success() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("f", new byte[]{});
        when(verificacionService.subirDocumentosIdentidad(1L, file, file)).thenReturn("url");
        
        // Act
        ResponseEntity<String> res = controller.subirDocumentosIdentidad(1L, file, file);

        // Assert
        assertEquals(200, res.getStatusCode().value());
        assertEquals("url", res.getBody());
    }

    @Test
    void subirCertificadoAntecedentes_Success() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("f", new byte[]{});
        when(verificacionService.subirCertificadoAntecedentes(1L, file)).thenReturn("url_cert");
        
        // Act
        ResponseEntity<String> res = controller.subirCertificadoAntecedentes(1L, file);

        // Assert
        assertEquals(200, res.getStatusCode().value());
        assertEquals("url_cert", res.getBody());
    }

    @Test
    void subirFotoPerfilVerificada_Success() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("f", new byte[]{});
        when(verificacionService.subirFotoPerfilVerificada(1L, file)).thenReturn("url_foto");
        
        // Act
        ResponseEntity<String> res = controller.subirFotoPerfilVerificada(1L, file);

        // Assert
        assertEquals(200, res.getStatusCode().value());
        assertEquals("url_foto", res.getBody());
    }

    @Test
    void obtenerEstadoVerificacion_Success() {
        // Arrange
        when(verificacionService.obtenerEstadoVerificacion(1L)).thenReturn(EstadoVerificacion.PENDIENTE_REVISION);
        
        // Act
        ResponseEntity<EstadoVerificacion> res = controller.obtenerEstadoVerificacion(1L);

        // Assert
        assertEquals(200, res.getStatusCode().value());
        assertEquals(EstadoVerificacion.PENDIENTE_REVISION, res.getBody());
    }
}
