package com.oficioYa.validator;

import com.oficioYa.exception.EstadoInvalidoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.persistence.entity.RolUsuario;
import com.oficioYa.persistence.entity.UsuarioEntity;
import com.oficioYa.repository.UsuarioRepository;
import com.oficioYa.validator.impl.SolicitudValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudValidatorImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitudValidatorImpl solicitudValidator;

    @Test
    @DisplayName("validarContratante - usuario existe, rol CONTRATANTE y activo no lanza excepción")
    void validarContratante_usuarioValido_noLanzaExcepcion() {
        UsuarioEntity contratante = UsuarioEntity.builder()
                .id(1L)
                .rol(RolUsuario.CONTRATANTE)
                .activo(true)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(contratante));

        assertDoesNotThrow(() -> solicitudValidator.validarContratante(1L));
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("validarContratante - usuario no existe lanza UsuarioNoEncontradoException")
    void validarContratante_usuarioInexistente_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        UsuarioNoEncontradoException ex = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> solicitudValidator.validarContratante(99L)
        );

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("validarContratante - usuario no tiene rol CONTRATANTE lanza EstadoInvalidoException")
    void validarContratante_rolNoContratante_lanzaExcepcion() {
        UsuarioEntity trabajador = UsuarioEntity.builder()
                .id(2L)
                .rol(RolUsuario.TRABAJADOR)
                .activo(true)
                .build();

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(trabajador));

        EstadoInvalidoException ex = assertThrows(
                EstadoInvalidoException.class,
                () -> solicitudValidator.validarContratante(2L)
        );

        assertEquals("Solo un contratante puede crear solicitudes", ex.getMessage());
    }

    @Test
    @DisplayName("validarContratante - usuario inactivo lanza EstadoInvalidoException")
    void validarContratante_usuarioInactivo_lanzaExcepcion() {
        UsuarioEntity contratanteInactivo = UsuarioEntity.builder()
                .id(3L)
                .rol(RolUsuario.CONTRATANTE)
                .activo(false)
                .build();

        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(contratanteInactivo));

        EstadoInvalidoException ex = assertThrows(
                EstadoInvalidoException.class,
                () -> solicitudValidator.validarContratante(3L)
        );

        assertEquals("La cuenta del contratante se encuentra inactiva", ex.getMessage());
    }
}
