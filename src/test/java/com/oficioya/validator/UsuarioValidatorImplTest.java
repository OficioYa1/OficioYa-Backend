package com.oficioYa.validator;

import com.oficioYa.exception.CorreoYaRegistradoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.repository.UsuarioRepository;
import com.oficioYa.validator.impl.UsuarioValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioValidatorImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioValidatorImpl usuarioValidator;

    // ══════════════════════════════════════════════════════════════════
    //  validarCorreoUnico
    // ══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("validarCorreoUnico - correo no registrado no lanza excepcion")
    void validarCorreoUnico_correoNoRegistrado_noLanzaExcepcion() {
        // Arrange
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> usuarioValidator.validarCorreoUnico("nuevo@test.com"));
    }

    @Test
    @DisplayName("validarCorreoUnico - correo ya registrado lanza CorreoYaRegistradoException con mensaje")
    void validarCorreoUnico_correoYaRegistrado_lanzaCorreoYaRegistradoExceptionConMensaje() {
        // Arrange
        when(usuarioRepository.existsByCorreo("repetido@test.com")).thenReturn(true);

        // Act & Assert
        CorreoYaRegistradoException ex = assertThrows(
                CorreoYaRegistradoException.class,
                () -> usuarioValidator.validarCorreoUnico("repetido@test.com")
        );
        assertTrue(ex.getMessage().contains("repetido@test.com"));
    }

    // ══════════════════════════════════════════════════════════════════
    //  validarExistencia
    // ══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("validarExistencia - usuario existente no lanza excepcion")
    void validarExistencia_usuarioExistente_noLanzaExcepcion() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> usuarioValidator.validarExistencia(1L));
    }

    @Test
    @DisplayName("validarExistencia - usuario no encontrado lanza UsuarioNoEncontradoException con ID en mensaje")
    void validarExistencia_usuarioNoEncontrado_lanzaUsuarioNoEncontradoExceptionConId() {
        // Arrange
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        UsuarioNoEncontradoException ex = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioValidator.validarExistencia(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }
}
