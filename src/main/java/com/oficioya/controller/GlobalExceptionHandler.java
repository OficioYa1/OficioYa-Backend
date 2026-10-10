package com.oficioYa.controller;

import com.oficioYa.exception.CorreoYaRegistradoException;
import com.oficioYa.exception.EstadoInvalidoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.exception.RecursoNoEncontradoException;
import com.oficioYa.model.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.oficioYa.exception.ConflictoException;
import com.oficioYa.model.exception.ReglaDeNegocioException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String mensajes = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(400, "Datos de entrada inválidos", mensajes, request.getRequestURI()));
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsuarioNoEncontrado(
            UsuarioNoEncontradoException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(404, "Recurso no encontrado", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleRecursoNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(404, "Recurso no encontrado", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleCorreoYaRegistrado(
            CorreoYaRegistradoException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(409, "Conflicto de unicidad", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ResponseEntity<ErrorResponseDTO> handleEstadoInvalido(
            EstadoInvalidoException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponseDTO(422, "Transición de estado inválida", ex.getMessage(), request.getRequestURI()));
    }


    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflicto(
            ConflictoException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(409, "Conflicto de negocio", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<ErrorResponseDTO> handleReglaDeNegocio(
            ReglaDeNegocioException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponseDTO(422, "Regla de negocio incumplida", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponseDTO> handleFormatoInvalido(
            Exception ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(400, "Formato de datos inválido",
                        "El cuerpo o los parámetros de la petición tienen un formato o valor no válido", request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenerico(
            Exception ex, HttpServletRequest request) {

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(500, "Error interno del servidor", "Ocurrió un error inesperado en el sistema.", request.getRequestURI()));
    }
}
