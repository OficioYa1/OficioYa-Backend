package com.oficioya.validator;

import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.PerfilTrabajador;
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

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
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

    private FranjaDisponibilidad franja(DayOfWeek dia, String inicio, String fin) {
        return FranjaDisponibilidad.builder().dia(dia)
                .horaInicio(LocalTime.parse(inicio)).horaFin(LocalTime.parse(fin)).build();
    }

    @Test
    @DisplayName("validarFranjasDisponibilidad - franjas válidas y consecutivas no lanzan excepción")
    void validarFranjas_validasYConsecutivas_noLanza() {
        // Arrange
        List<FranjaDisponibilidad> franjas = List.of(
                franja(DayOfWeek.MONDAY, "08:00", "12:00"),
                franja(DayOfWeek.MONDAY, "12:00", "17:00"),
                franja(DayOfWeek.TUESDAY, "08:00", "12:00"));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarFranjasDisponibilidad(franjas));
    }

    @Test
    @DisplayName("validarFranjasDisponibilidad - lista vacía es válida")
    void validarFranjas_listaVacia_noLanza() {
        // Act & Assert
        assertDoesNotThrow(() -> validator.validarFranjasDisponibilidad(List.of()));
    }

    @Test
    @DisplayName("validarFranjasDisponibilidad - inicio igual o posterior al fin lanza ReglaDeNegocioException")
    void validarFranjas_inicioPosteriorAlFin_lanzaReglaDeNegocio() {
        // Arrange
        List<FranjaDisponibilidad> franjas = List.of(franja(DayOfWeek.MONDAY, "17:00", "08:00"));

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarFranjasDisponibilidad(franjas));
    }

    @Test
    @DisplayName("validarFranjasDisponibilidad - franjas solapadas el mismo día lanzan ReglaDeNegocioException")
    void validarFranjas_solapadasMismoDia_lanzaReglaDeNegocio() {
        // Arrange
        List<FranjaDisponibilidad> franjas = List.of(
                franja(DayOfWeek.MONDAY, "08:00", "12:00"),
                franja(DayOfWeek.MONDAY, "11:00", "15:00"));

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarFranjasDisponibilidad(franjas));
    }

    @Test
    @DisplayName("validarFranjasDisponibilidad - mismas horas en días distintos no se consideran solape")
    void validarFranjas_mismasHorasDiasDistintos_noLanza() {
        // Arrange
        List<FranjaDisponibilidad> franjas = List.of(
                franja(DayOfWeek.MONDAY, "08:00", "12:00"),
                franja(DayOfWeek.TUESDAY, "08:00", "12:00"));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarFranjasDisponibilidad(franjas));
    }

    @Test
    @DisplayName("validarPuedeActivarDisponibleAhora - perfil inactivo con zona no lanza excepción")
    void validarPuedeActivar_perfilInactivoConZona_noLanza() {
        // Arrange
        PerfilTrabajador perfil = PerfilTrabajador.builder().id(1L).zonaCobertura("Norte").disponibleAhora(false).build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarPuedeActivarDisponibleAhora(perfil));
    }

    @Test
    @DisplayName("validarPuedeActivarDisponibleAhora - ya activo lanza EstadoInvalidoException")
    void validarPuedeActivar_yaActivo_lanzaEstadoInvalido() {
        // Arrange
        PerfilTrabajador perfil = PerfilTrabajador.builder().id(1L).zonaCobertura("Norte").disponibleAhora(true).build();

        // Act & Assert
        assertThrows(EstadoInvalidoException.class, () -> validator.validarPuedeActivarDisponibleAhora(perfil));
    }

    @Test
    @DisplayName("validarPuedeActivarDisponibleAhora - sin zona de cobertura lanza ReglaDeNegocioException")
    void validarPuedeActivar_sinZona_lanzaReglaDeNegocio() {
        // Arrange
        PerfilTrabajador perfil = PerfilTrabajador.builder().id(1L).zonaCobertura("  ").build();

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarPuedeActivarDisponibleAhora(perfil));
    }

    @Test
    @DisplayName("validarPuedeDesactivarDisponibleAhora - perfil activo no lanza excepción")
    void validarPuedeDesactivar_perfilActivo_noLanza() {
        // Arrange
        PerfilTrabajador perfil = PerfilTrabajador.builder().id(1L).disponibleAhora(true).build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarPuedeDesactivarDisponibleAhora(perfil));
    }

    @Test
    @DisplayName("validarPuedeDesactivarDisponibleAhora - ya inactivo lanza EstadoInvalidoException")
    void validarPuedeDesactivar_yaInactivo_lanzaEstadoInvalido() {
        // Arrange
        PerfilTrabajador perfil = PerfilTrabajador.builder().id(1L).disponibleAhora(false).build();

        // Act & Assert
        assertThrows(EstadoInvalidoException.class, () -> validator.validarPuedeDesactivarDisponibleAhora(perfil));
    }
}
