package com.oficioya.validator;

import com.oficioya.exception.CorreoYaRegistradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validator.impl.UsuarioValidatorImpl;
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

    @Test
    void validarCorreoUnico_whenEmailNotExists_doesNotThrow() {
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);

        assertDoesNotThrow(() -> usuarioValidator.validarCorreoUnico("nuevo@test.com"));
    }

    @Test
    void validarCorreoUnico_whenEmailAlreadyExists_throwsCorreoYaRegistradoException() {
        when(usuarioRepository.existsByCorreo("repetido@test.com")).thenReturn(true);

        CorreoYaRegistradoException ex = assertThrows(
                CorreoYaRegistradoException.class,
                () -> usuarioValidator.validarCorreoUnico("repetido@test.com")
        );

        assertTrue(ex.getMessage().contains("repetido@test.com"));
    }

    @Test
    void validarExistencia_whenUserExists_doesNotThrow() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> usuarioValidator.validarExistencia(1L));
    }

    @Test
    void validarExistencia_whenUserNotFound_throwsUsuarioNoEncontradoException() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        UsuarioNoEncontradoException ex = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioValidator.validarExistencia(99L)
        );

        assertTrue(ex.getMessage().contains("99"));
    }
}
