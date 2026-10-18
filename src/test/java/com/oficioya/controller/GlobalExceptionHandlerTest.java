package com.oficioya.controller;

import com.oficioya.exception.*;
import com.oficioya.model.dto.response.ErrorResponseDTO;
import com.oficioya.model.exception.ReglaDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        lenient().when(request.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    @DisplayName("handleUsuarioNoEncontrado retorna 404")
    void testUsuarioNoEncontrado() {
        // Arrange
        UsuarioNoEncontradoException ex = new UsuarioNoEncontradoException("Usuario no existe");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleUsuarioNoEncontrado(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().status());
        assertEquals("Usuario no existe", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleRecursoNoEncontrado retorna 404")
    void testRecursoNoEncontrado() {
        // Arrange
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("Recurso faltante");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleRecursoNoEncontrado(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().status());
        assertEquals("Recurso faltante", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleCorreoYaRegistrado retorna 409")
    void testCorreoYaRegistrado() {
        // Arrange
        CorreoYaRegistradoException ex = new CorreoYaRegistradoException("Correo ya existe");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleCorreoYaRegistrado(ex, request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().status());
        assertEquals("Correo ya existe", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleConflicto retorna 409")
    void testConflicto() {
        // Arrange
        ConflictoException ex = new ConflictoException("Conflicto detectado");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleConflicto(ex, request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().status());
        assertEquals("Conflicto detectado", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleEstadoInvalido retorna 422")
    void testEstadoInvalido() {
        // Arrange
        EstadoInvalidoException ex = new EstadoInvalidoException("Estado no permitido");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleEstadoInvalido(ex, request);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
        assertEquals(422, resp.getBody().status());
        assertEquals("Estado no permitido", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleEstadoInvalido con excepcion de dominio retorna 422")
    void testEstadoInvalidoDominio() {
        // Arrange
        com.oficioya.model.exception.EstadoInvalidoException ex = new com.oficioya.model.exception.EstadoInvalidoException("Transicion no permitida");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleEstadoInvalido(ex, request);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
        assertEquals(422, resp.getBody().status());
        assertEquals("Transicion no permitida", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleReglaDeNegocio retorna 422")
    void testReglaDeNegocio() {
        // Arrange
        ReglaDeNegocioException ex = new ReglaDeNegocioException("Regla rota");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleReglaDeNegocio(ex, request);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
        assertEquals(422, resp.getBody().status());
        assertEquals("Regla rota", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleFormatoInvalido retorna 400")
    void testFormatoInvalido() {
        // Arrange
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON malformado", (HttpInputMessage) null);

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleFormatoInvalido(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().status());
    }

    @Test
    @DisplayName("handleValidacion retorna 400 con lista de campos fallidos")
    void testValidacion() throws NoSuchMethodException {
        // Arrange
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objeto");
        bindingResult.addError(new FieldError("objeto", "correo", "no debe ser nulo"));

        MethodParameter parameter = new MethodParameter(
                GlobalExceptionHandlerTest.class.getDeclaredMethod("setUp"), -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleValidacion(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().status());
        assertTrue(resp.getBody().mensaje().contains("correo: no debe ser nulo"));
    }

    @Test
    @DisplayName("handleAutenticacion retorna 401")
    void testAutenticacion() {
        // Arrange
        org.springframework.security.authentication.BadCredentialsException ex = 
                new org.springframework.security.authentication.BadCredentialsException("Credenciales inválidas");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleAutenticacion(ex, request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        assertEquals(401, resp.getBody().status());
        assertEquals("Credenciales inválidas", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleAccesoDenegado retorna 403")
    void testAccesoDenegado() {
        // Arrange
        org.springframework.security.access.AccessDeniedException ex = 
                new org.springframework.security.access.AccessDeniedException("Acceso denegado");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleAccesoDenegado(ex, request);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
        assertEquals(403, resp.getBody().status());
        assertEquals("No tiene los permisos requeridos para acceder a este recurso.", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleGenerico retorna 500")
    void testGenerico() {
        // Arrange
        RuntimeException ex = new RuntimeException("Falla desconocida");

        // Act
        ResponseEntity<ErrorResponseDTO> resp = handler.handleGenerico(ex, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals(500, resp.getBody().status());
    }
}
