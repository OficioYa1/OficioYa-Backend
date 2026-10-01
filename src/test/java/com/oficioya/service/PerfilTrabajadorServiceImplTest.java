package com.oficioya.service;

import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.service.impl.PerfilTrabajadorServiceImpl;
import com.oficioya.validator.IPerfilTrabajadorValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilTrabajadorServiceImplTest {

    @Mock private PerfilTrabajadorRepository perfilRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private IPerfilTrabajadorValidator validator;
    @Mock private PerfilTrabajadorEntityMapper entityMapper;

    @InjectMocks private PerfilTrabajadorServiceImpl service;

    private PerfilTrabajador dominioEntrada(Long usuarioId) {
        return PerfilTrabajador.builder()
                .usuario(Usuario.builder().id(usuarioId).build())
                .descripcion("Plomero con experiencia")
                .build();
    }

    @Test
    @DisplayName("crearPerfil - cuenta válida guarda el perfil con valores iniciales y retorna con ID")
    void crearPerfil_cuentaValida_guardaYRetornaConId() {
        // Arrange
        PerfilTrabajador entrada = dominioEntrada(7L);
        UsuarioEntity usuario = UsuarioEntity.builder().id(7L).build();
        PerfilTrabajadorEntity entidad = PerfilTrabajadorEntity.builder().descripcion("Plomero con experiencia").build();
        PerfilTrabajadorEntity guardada = PerfilTrabajadorEntity.builder().id(1L).usuario(usuario).build();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).usuario(Usuario.builder().id(7L).build()).build();
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(entityMapper.toEntity(entrada)).thenReturn(entidad);
        when(perfilRepository.save(entidad)).thenReturn(guardada);
        when(entityMapper.toDomain(guardada)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.crearPerfil(entrada);

        // Assert
        assertEquals(1L, resultado.getId());
        assertEquals(0.0, entrada.getCalificacionPromedio());
        assertEquals(0, entrada.getTrabajosCompletados());
        assertFalse(entrada.isDisponibleAhora());
        ArgumentCaptor<PerfilTrabajadorEntity> captor = ArgumentCaptor.forClass(PerfilTrabajadorEntity.class);
        verify(perfilRepository).save(captor.capture());
        assertSame(usuario, captor.getValue().getUsuario());
        verify(validator, times(1)).validarCuentaElegible(7L);
        verify(validator, times(1)).validarPerfilNoExiste(7L);
    }

    @Test
    @DisplayName("crearPerfil - usuario inexistente propaga la excepción del validator y no guarda")
    void crearPerfil_usuarioInexistente_propagaNotFound() {
        // Arrange
        PerfilTrabajador entrada = dominioEntrada(99L);
        doThrow(new UsuarioNoEncontradoException("No existe")).when(validator).validarCuentaElegible(99L);

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> service.crearPerfil(entrada));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("crearPerfil - perfil duplicado propaga ConflictoException y no guarda")
    void crearPerfil_perfilDuplicado_propagaConflicto() {
        // Arrange
        PerfilTrabajador entrada = dominioEntrada(7L);
        doThrow(new ConflictoException("Ya tiene perfil")).when(validator).validarPerfilNoExiste(7L);

        // Act & Assert
        assertThrows(ConflictoException.class, () -> service.crearPerfil(entrada));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("crearPerfil - cuenta no elegible propaga ReglaDeNegocioException y no guarda")
    void crearPerfil_cuentaNoElegible_propagaReglaDeNegocio() {
        // Arrange
        PerfilTrabajador entrada = dominioEntrada(7L);
        doThrow(new ReglaDeNegocioException("No verificada")).when(validator).validarCuentaElegible(7L);

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> service.crearPerfil(entrada));
        verify(validator, never()).validarPerfilNoExiste(any());
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("crearPerfil - usuario desaparece tras validar lanza UsuarioNoEncontradoException")
    void crearPerfil_usuarioNoCargable_lanzaNotFound() {
        // Arrange
        PerfilTrabajador entrada = dominioEntrada(7L);
        when(usuarioRepository.findById(7L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> service.crearPerfil(entrada));
        verify(perfilRepository, never()).save(any());
    }

    // ---------------- RF-04: zona de cobertura ----------------

    @Test
    @DisplayName("actualizarZonaCobertura - perfil existente guarda la zona sin espacios sobrantes")
    void actualizarZonaCobertura_perfilExistente_guardaZona() {
        // Arrange
        PerfilTrabajadorEntity entidad = PerfilTrabajadorEntity.builder().id(1L).build();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).zonaCobertura("Chapinero").build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(perfilRepository.save(entidad)).thenReturn(entidad);
        when(entityMapper.toDomain(entidad)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.actualizarZonaCobertura(1L, "  Chapinero ");

        // Assert
        assertEquals("Chapinero", entidad.getZonaCobertura());
        assertEquals("Chapinero", resultado.getZonaCobertura());
        verify(perfilRepository, times(1)).save(entidad);
    }

    @Test
    @DisplayName("actualizarZonaCobertura - perfil inexistente lanza RecursoNoEncontradoException")
    void actualizarZonaCobertura_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarZonaCobertura(99L, "Usaquén"));
        verify(perfilRepository, never()).save(any());
    }

    // ---------------- RF-05: tarifa ----------------

    @Test
    @DisplayName("actualizarTarifa - perfil existente guarda la tarifa")
    void actualizarTarifa_perfilExistente_guardaTarifa() {
        // Arrange
        PerfilTrabajadorEntity entidad = PerfilTrabajadorEntity.builder().id(1L).build();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).tarifaPorHora(new BigDecimal("35000")).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(perfilRepository.save(entidad)).thenReturn(entidad);
        when(entityMapper.toDomain(entidad)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.actualizarTarifa(1L, new BigDecimal("35000"));

        // Assert
        assertEquals(new BigDecimal("35000"), entidad.getTarifaPorHora());
        assertEquals(new BigDecimal("35000"), resultado.getTarifaPorHora());
        verify(perfilRepository, times(1)).save(entidad);
    }

    @Test
    @DisplayName("actualizarTarifa - perfil inexistente lanza RecursoNoEncontradoException")
    void actualizarTarifa_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarTarifa(99L, new BigDecimal("1000")));
        verify(perfilRepository, never()).save(any());
    }
}
