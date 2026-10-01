package com.oficioya.validator;

import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validator.impl.PerfilTrabajadorValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilTrabajadorValidatorImplTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PerfilTrabajadorRepository perfilRepository;

    @InjectMocks private PerfilTrabajadorValidatorImpl validator;

    private UsuarioEntity usuario(RolUsuario rol, boolean activo, boolean correoVerificado, boolean telefonoVerificado) {
        return UsuarioEntity.builder().id(1L).rol(rol).activo(activo)
                .correoVerificado(correoVerificado).telefonoVerificado(telefonoVerificado).build();
    }

    @Test
    @DisplayName("validarCuentaElegible - trabajador activo con correo verificado no lanza excepción")
    void validarCuentaElegible_cuentaValida_noLanza() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(RolUsuario.TRABAJADOR, true, true, false)));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarCuentaElegible(1L));
    }

    @Test
    @DisplayName("validarCuentaElegible - solo teléfono verificado también es válido")
    void validarCuentaElegible_soloTelefonoVerificado_noLanza() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(RolUsuario.TRABAJADOR, true, false, true)));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarCuentaElegible(1L));
    }

    @Test
    @DisplayName("validarCuentaElegible - usuario inexistente lanza UsuarioNoEncontradoException")
    void validarCuentaElegible_usuarioInexistente_lanzaNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> validator.validarCuentaElegible(1L));
    }

    @Test
    @DisplayName("validarCuentaElegible - rol CONTRATANTE lanza ReglaDeNegocioException")
    void validarCuentaElegible_rolContratante_lanzaReglaDeNegocio() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(RolUsuario.CONTRATANTE, true, true, true)));

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarCuentaElegible(1L));
    }

    @Test
    @DisplayName("validarCuentaElegible - cuenta inactiva lanza ReglaDeNegocioException")
    void validarCuentaElegible_cuentaInactiva_lanzaReglaDeNegocio() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(RolUsuario.TRABAJADOR, false, true, true)));

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarCuentaElegible(1L));
    }

    @Test
    @DisplayName("validarCuentaElegible - sin correo ni teléfono verificado lanza ReglaDeNegocioException")
    void validarCuentaElegible_sinVerificar_lanzaReglaDeNegocio() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(RolUsuario.TRABAJADOR, true, false, false)));

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarCuentaElegible(1L));
    }

    @Test
    @DisplayName("validarPerfilNoExiste - usuario sin perfil no lanza excepción")
    void validarPerfilNoExiste_sinPerfil_noLanza() {
        // Arrange
        when(perfilRepository.existsByUsuarioId(1L)).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarPerfilNoExiste(1L));
    }

    @Test
    @DisplayName("validarPerfilNoExiste - usuario con perfil lanza ConflictoException")
    void validarPerfilNoExiste_conPerfil_lanzaConflicto() {
        // Arrange
        when(perfilRepository.existsByUsuarioId(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictoException.class, () -> validator.validarPerfilNoExiste(1L));
    }

    @Test
    @DisplayName("validarPerfilExiste - perfil inexistente lanza RecursoNoEncontradoException")
    void validarPerfilExiste_inexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.existsById(5L)).thenReturn(false);

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> validator.validarPerfilExiste(5L));
    }

    @Test
    @DisplayName("validarPerfilExiste - perfil existente no lanza excepción")
    void validarPerfilExiste_existente_noLanza() {
        // Arrange
        when(perfilRepository.existsById(5L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarPerfilExiste(5L));
    }
}
