package com.oficioYa.controller;

import com.oficioYa.exception.*;
import com.oficioYa.model.dto.response.ErrorResponseDTO;
import com.oficioYa.model.exception.ReglaDeNegocioException;
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
        UsuarioNoEncontradoException ex = new UsuarioNoEncontradoException("Usuario no existe");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleUsuarioNoEncontrado(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().status());
        assertEquals("Usuario no existe", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleRecursoNoEncontrado retorna 404")
    void testRecursoNoEncontrado() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("Recurso faltante");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleRecursoNoEncontrado(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().status());
        assertEquals("Recurso faltante", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleCorreoYaRegistrado retorna 409")
    void testCorreoYaRegistrado() {
        CorreoYaRegistradoException ex = new CorreoYaRegistradoException("Correo ya existe");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleCorreoYaRegistrado(ex, request);

        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().status());
        assertEquals("Correo ya existe", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleConflicto retorna 409")
    void testConflicto() {
        ConflictoException ex = new ConflictoException("Conflicto detectado");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleConflicto(ex, request);

        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().status());
        assertEquals("Conflicto detectado", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleEstadoInvalido retorna 422")
    void testEstadoInvalido() {
        EstadoInvalidoException ex = new EstadoInvalidoException("Estado no permitido");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleEstadoInvalido(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
        assertEquals(422, resp.getBody().status());
        assertEquals("Estado no permitido", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleReglaDeNegocio retorna 422")
    void testReglaDeNegocio() {
        ReglaDeNegocioException ex = new ReglaDeNegocioException("Regla rota");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleReglaDeNegocio(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
        assertEquals(422, resp.getBody().status());
        assertEquals("Regla rota", resp.getBody().mensaje());
    }

    @Test
    @DisplayName("handleFormatoInvalido retorna 400")
    void testFormatoInvalido() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON malformado", (HttpInputMessage) null);
        ResponseEntity<ErrorResponseDTO> resp = handler.handleFormatoInvalido(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().status());
    }

    @Test
    @DisplayName("handleValidacion retorna 400 con lista de campos fallidos")
    void testValidacion() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objeto");
        bindingResult.addError(new FieldError("objeto", "correo", "no debe ser nulo"));

        MethodParameter parameter = new MethodParameter(
                GlobalExceptionHandlerTest.class.getDeclaredMethod("setUp"), -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponseDTO> resp = handler.handleValidacion(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().status());
        assertTrue(resp.getBody().mensaje().contains("correo: no debe ser nulo"));
    }

    @Test
    @DisplayName("handleGenerico retorna 500")
    void testGenerico() {
        RuntimeException ex = new RuntimeException("Falla desconocida");
        ResponseEntity<ErrorResponseDTO> resp = handler.handleGenerico(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals(500, resp.getBody().status());
    }
}
