package com.oficioya.service;

import com.oficioya.repository.OficioRepository;
import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.RecursoNoEncontradoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.MetodoPago;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.domain.Usuario;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.persistence.entity.FranjaDisponibilidadEmbeddable;
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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilTrabajadorServiceImplTest {

    @Mock private PerfilTrabajadorRepository perfilRepository;
    @Mock private OficioRepository oficioRepository;
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

    // ───────────────────────── RF-04 / 05 / 06 / 60 ─────────────────────────

    private PerfilTrabajadorEntity entidadExistente() {
        return PerfilTrabajadorEntity.builder().id(1L).build();
    }

    @Test
    @DisplayName("actualizarZonaCobertura - perfil existente guarda la zona sin espacios sobrantes")
    void actualizarZonaCobertura_perfilExistente_guardaZona() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).zonaCobertura("Chapinero").build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(perfilRepository.save(entidad)).thenReturn(entidad);
        when(entityMapper.toDomain(entidad)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.actualizarZonaCobertura(1L, "  Chapinero ");

        // Assert
        assertEquals("Chapinero", entidad.getZonaCobertura());
        assertSame(esperado, resultado);
        verify(perfilRepository, times(1)).save(entidad);
    }

    @Test
    @DisplayName("actualizarZonaCobertura - perfil inexistente lanza RecursoNoEncontradoException")
    void actualizarZonaCobertura_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarZonaCobertura(99L, "Norte"));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarTarifa - perfil existente guarda la tarifa")
    void actualizarTarifa_perfilExistente_guardaTarifa() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).tarifaPorHora(new BigDecimal("35000")).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(perfilRepository.save(entidad)).thenReturn(entidad);
        when(entityMapper.toDomain(entidad)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.actualizarTarifa(1L, new BigDecimal("35000"));

        // Assert
        assertEquals(new BigDecimal("35000"), entidad.getTarifaPorHora());
        assertSame(esperado, resultado);
    }

    @Test
    @DisplayName("actualizarTarifa - perfil inexistente lanza RecursoNoEncontradoException")
    void actualizarTarifa_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarTarifa(99L, BigDecimal.TEN));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarDisponibilidadSemanal - reemplaza las franjas anteriores por las nuevas")
    void actualizarDisponibilidadSemanal_franjasValidas_reemplazaFranjas() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        entidad.getDisponibilidadSemanal().add(FranjaDisponibilidadEmbeddable.builder()
                .dia(DayOfWeek.FRIDAY).horaInicio(LocalTime.of(6, 0)).horaFin(LocalTime.of(7, 0)).build());
        List<FranjaDisponibilidad> franjas = List.of(FranjaDisponibilidad.builder()
                .dia(DayOfWeek.MONDAY).horaInicio(LocalTime.of(8, 0)).horaFin(LocalTime.of(17, 0)).build());
        FranjaDisponibilidadEmbeddable nueva = FranjaDisponibilidadEmbeddable.builder()
                .dia(DayOfWeek.MONDAY).horaInicio(LocalTime.of(8, 0)).horaFin(LocalTime.of(17, 0)).build();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(entityMapper.toFranjasEntity(franjas)).thenReturn(List.of(nueva));
        when(perfilRepository.save(entidad)).thenReturn(entidad);
        when(entityMapper.toDomain(entidad)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.actualizarDisponibilidadSemanal(1L, franjas);

        // Assert
        assertSame(esperado, resultado);
        assertEquals(List.of(nueva), entidad.getDisponibilidadSemanal());
        verify(validator, times(1)).validarFranjasDisponibilidad(franjas);
    }

    @Test
    @DisplayName("actualizarDisponibilidadSemanal - franjas inválidas propagan ReglaDeNegocioException y no guardan")
    void actualizarDisponibilidadSemanal_franjasInvalidas_propagaReglaDeNegocio() {
        // Arrange
        List<FranjaDisponibilidad> franjas = List.of();
        doThrow(new ReglaDeNegocioException("Solapadas")).when(validator).validarFranjasDisponibilidad(franjas);

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> service.actualizarDisponibilidadSemanal(1L, franjas));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarDisponibilidadSemanal - perfil inexistente lanza RecursoNoEncontradoException")
    void actualizarDisponibilidadSemanal_perfilInexistente_lanzaNotFound() {
        // Arrange
        List<FranjaDisponibilidad> franjas = List.of();
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarDisponibilidadSemanal(99L, franjas));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarMetodosPago - reemplaza los métodos de pago anteriores")
    void actualizarMetodosPago_perfilExistente_reemplazaMetodos() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        entidad.getMetodosPago().add(MetodoPago.EFECTIVO);
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(perfilRepository.save(entidad)).thenReturn(entidad);
        when(entityMapper.toDomain(entidad)).thenReturn(esperado);

        // Act
        PerfilTrabajador resultado = service.actualizarMetodosPago(1L, Set.of(MetodoPago.NEQUI, MetodoPago.DAVIPLATA));

        // Assert
        assertSame(esperado, resultado);
        assertEquals(Set.of(MetodoPago.NEQUI, MetodoPago.DAVIPLATA), entidad.getMetodosPago());
    }

    @Test
    @DisplayName("actualizarMetodosPago - perfil inexistente lanza RecursoNoEncontradoException")
    void actualizarMetodosPago_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarMetodosPago(99L, Set.of(MetodoPago.NEQUI)));
        verify(perfilRepository, never()).save(any());
    }

    // ───────────────────────── RF-30 / 31 ─────────────────────────

    @Test
    @DisplayName("activarDisponibleAhora - perfil elegible queda disponible")
    void activarDisponibleAhora_perfilElegible_activa() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        PerfilTrabajador dominio = PerfilTrabajador.builder().id(1L).zonaCobertura("Norte").build();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).disponibleAhora(true).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(entityMapper.toDomain(entidad)).thenReturn(dominio, esperado);
        when(perfilRepository.save(entidad)).thenReturn(entidad);

        // Act
        PerfilTrabajador resultado = service.activarDisponibleAhora(1L);

        // Assert
        assertTrue(entidad.isDisponibleAhora());
        assertSame(esperado, resultado);
        verify(validator, times(1)).validarPuedeActivarDisponibleAhora(dominio);
    }

    @Test
    @DisplayName("activarDisponibleAhora - regla incumplida propaga la excepción y no guarda")
    void activarDisponibleAhora_reglaIncumplida_propagaExcepcion() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        PerfilTrabajador dominio = PerfilTrabajador.builder().id(1L).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(entityMapper.toDomain(entidad)).thenReturn(dominio);
        doThrow(new ReglaDeNegocioException("Sin zona")).when(validator).validarPuedeActivarDisponibleAhora(dominio);

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> service.activarDisponibleAhora(1L));
        assertFalse(entidad.isDisponibleAhora());
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("activarDisponibleAhora - perfil inexistente lanza RecursoNoEncontradoException")
    void activarDisponibleAhora_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.activarDisponibleAhora(99L));
    }

    @Test
    @DisplayName("desactivarDisponibleAhora - perfil activo queda no disponible")
    void desactivarDisponibleAhora_perfilActivo_desactiva() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        entidad.setDisponibleAhora(true);
        PerfilTrabajador dominio = PerfilTrabajador.builder().id(1L).disponibleAhora(true).build();
        PerfilTrabajador esperado = PerfilTrabajador.builder().id(1L).disponibleAhora(false).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(entityMapper.toDomain(entidad)).thenReturn(dominio, esperado);
        when(perfilRepository.save(entidad)).thenReturn(entidad);

        // Act
        PerfilTrabajador resultado = service.desactivarDisponibleAhora(1L);

        // Assert
        assertFalse(entidad.isDisponibleAhora());
        assertSame(esperado, resultado);
        verify(validator, times(1)).validarPuedeDesactivarDisponibleAhora(dominio);
    }

    @Test
    @DisplayName("desactivarDisponibleAhora - estado inválido propaga EstadoInvalidoException y no guarda")
    void desactivarDisponibleAhora_estadoInvalido_propagaExcepcion() {
        // Arrange
        PerfilTrabajadorEntity entidad = entidadExistente();
        PerfilTrabajador dominio = PerfilTrabajador.builder().id(1L).build();
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(entityMapper.toDomain(entidad)).thenReturn(dominio);
        doThrow(new EstadoInvalidoException("No estaba activo")).when(validator).validarPuedeDesactivarDisponibleAhora(dominio);

        // Act & Assert
        assertThrows(EstadoInvalidoException.class, () -> service.desactivarDisponibleAhora(1L));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("desactivarDisponibleAhora - perfil inexistente lanza RecursoNoEncontradoException")
    void desactivarDisponibleAhora_perfilInexistente_lanzaNotFound() {
        // Arrange
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> service.desactivarDisponibleAhora(99L));
    }

    // =========================================================================
    // TESTS PARA RF-02: REGISTRAR OFICIO PRINCIPAL
    // =========================================================================

    @Test
    @DisplayName("registrarOficioPrincipal - Flujo Exitoso")
    void registrarOficioPrincipal_flujoExitoso() {
        // Arrange
        Long perfilId = 1L;
        Long oficioId = 10L;

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        OficioEntity oficio = new OficioEntity();
        oficio.setId(oficioId);
        oficio.setActivo(true);

        com.oficioya.model.domain.PerfilTrabajador domain = new com.oficioya.model.domain.PerfilTrabajador();
        domain.setId(perfilId);
        

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        when(oficioRepository.findById(oficioId)).thenReturn(java.util.Optional.of(oficio));
        when(perfilRepository.save(any(PerfilTrabajadorEntity.class))).thenReturn(perfil);
        when(entityMapper.toDomain(perfil)).thenReturn(domain);

        // Act
        PerfilTrabajador result = service.registrarOficioPrincipal(perfilId, oficioId);

        // Assert
        assertNotNull(result);
        assertEquals(perfilId, result.getId());
        verify(perfilRepository).findById(perfilId);
        verify(oficioRepository).findById(oficioId);
        verify(perfilRepository).save(perfil);
    }

    @Test
    @DisplayName("registrarOficioPrincipal - Oficio Inactivo lanza excepcion")
    void registrarOficioPrincipal_oficioInactivo_lanzaExcepcion() {
        // Arrange
        Long perfilId = 1L;
        Long oficioId = 10L;

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        OficioEntity oficio = new OficioEntity();
        oficio.setId(oficioId);
        oficio.setActivo(false);

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        when(oficioRepository.findById(oficioId)).thenReturn(java.util.Optional.of(oficio));

        // Act & Assert
        EstadoInvalidoException ex = assertThrows(EstadoInvalidoException.class, 
                () -> service.registrarOficioPrincipal(perfilId, oficioId));
        assertEquals("No se puede asignar un oficio inactivo como principal", ex.getMessage());
        verify(perfilRepository, never()).save(any());
    }

    @Test
    @DisplayName("registrarOficioPrincipal - Oficio No Encontrado lanza excepcion")
    void registrarOficioPrincipal_oficioNoEncontrado_lanzaExcepcion() {
        // Arrange
        Long perfilId = 1L;
        Long oficioId = 10L;

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        when(oficioRepository.findById(oficioId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, 
                () -> service.registrarOficioPrincipal(perfilId, oficioId));
        assertTrue(ex.getMessage().contains("Oficio no encontrado"));
        verify(perfilRepository, never()).save(any());
    }


    // =========================================================================
    // TESTS PARA RF-03: REGISTRAR OFICIOS SECUNDARIOS
    // =========================================================================

    @Test
    @DisplayName("registrarOficiosSecundarios - Flujo Exitoso")
    void registrarOficiosSecundarios_flujoExitoso() {
        // Arrange
        Long perfilId = 1L;
        List<Long> oficiosIds = List.of(2L, 3L);

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        OficioEntity oficio2 = new OficioEntity();
        oficio2.setId(2L);
        oficio2.setActivo(true);
        
        OficioEntity oficio3 = new OficioEntity();
        oficio3.setId(3L);
        oficio3.setActivo(true);

        com.oficioya.model.domain.PerfilTrabajador domain = new com.oficioya.model.domain.PerfilTrabajador();
        domain.setId(perfilId);

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        when(oficioRepository.findAllById(oficiosIds)).thenReturn(List.of(oficio2, oficio3));
        when(perfilRepository.save(any(PerfilTrabajadorEntity.class))).thenReturn(perfil);
        when(entityMapper.toDomain(perfil)).thenReturn(domain);

        // Act
        PerfilTrabajador result = service.registrarOficiosSecundarios(perfilId, oficiosIds);

        // Assert
        assertNotNull(result);
        assertEquals(perfilId, result.getId());
        verify(perfilRepository).findById(perfilId);
        verify(oficioRepository).findAllById(oficiosIds);
        verify(perfilRepository).save(perfil);
    }

    @Test
    @DisplayName("registrarOficiosSecundarios - Oficio faltante lanza excepcion")
    void registrarOficiosSecundarios_oficioFaltante_lanzaExcepcion() {
        // Arrange
        Long perfilId = 1L;
        List<Long> oficiosIds = List.of(2L, 99L);

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        OficioEntity oficio2 = new OficioEntity();
        oficio2.setId(2L);

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        // Devuelve solo 1 oficio, pero se solicitaron 2
        when(oficioRepository.findAllById(oficiosIds)).thenReturn(List.of(oficio2));

        // Act & Assert
        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, 
                () -> service.registrarOficiosSecundarios(perfilId, oficiosIds));
        assertTrue(ex.getMessage().contains("Uno o más oficios secundarios"));
    }
    
    @Test
    @DisplayName("registrarOficiosSecundarios - Oficio Inactivo lanza excepcion")
    void registrarOficiosSecundarios_oficioInactivo_lanzaExcepcion() {
        // Arrange
        Long perfilId = 1L;
        List<Long> oficiosIds = List.of(2L);

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        OficioEntity oficio2 = new OficioEntity();
        oficio2.setId(2L);
        oficio2.setActivo(false); // Inactivo
        oficio2.setNombre("Pintor");

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        when(oficioRepository.findAllById(oficiosIds)).thenReturn(List.of(oficio2));

        // Act & Assert
        EstadoInvalidoException ex = assertThrows(EstadoInvalidoException.class, 
                () -> service.registrarOficiosSecundarios(perfilId, oficiosIds));
        assertTrue(ex.getMessage().contains("inactivo y no puede ser asignado"));
    }
    
    @Test
    @DisplayName("registrarOficiosSecundarios - Es oficio principal lanza excepcion")
    void registrarOficiosSecundarios_oficioEsPrincipal_lanzaExcepcion() {
        // Arrange
        Long perfilId = 1L;
        List<Long> oficiosIds = List.of(2L);

        OficioEntity oficio2 = new OficioEntity();
        oficio2.setId(2L);
        oficio2.setActivo(true);
        oficio2.setNombre("Plomero");
        
        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);
        perfil.setOficioPrincipal(oficio2); // El mismo oficio ya es el principal

        when(perfilRepository.findById(perfilId)).thenReturn(java.util.Optional.of(perfil));
        when(oficioRepository.findAllById(oficiosIds)).thenReturn(List.of(oficio2));

        // Act & Assert
        EstadoInvalidoException ex = assertThrows(EstadoInvalidoException.class, 
                () -> service.registrarOficiosSecundarios(perfilId, oficiosIds));
        assertTrue(ex.getMessage().contains("ya es el oficio principal y no puede ser secundario"));
    }


    // =========================================================================
    // TESTS PARA RF-09: DETALLES ESPECIFICOS
    // =========================================================================

    @Test
    @DisplayName("actualizarDetallesEspecificos - Flujo Exitoso")
    void actualizarDetallesEspecificos_flujoExitoso() {
        // Arrange
        Long perfilId = 1L;
        String detalles = "Solo trabajo con pintura acrílica y rodillo especial.";

        PerfilTrabajadorEntity perfil = new PerfilTrabajadorEntity();
        perfil.setId(perfilId);

        PerfilTrabajador domain = new PerfilTrabajador();
        domain.setId(perfilId);
        domain.setDetallesEspecificos(detalles);

        when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(PerfilTrabajadorEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(entityMapper.toDomain(any(PerfilTrabajadorEntity.class))).thenReturn(domain);

        // Act
        PerfilTrabajador result = service.actualizarDetallesEspecificos(perfilId, detalles);

        // Assert
        assertNotNull(result);
        assertEquals(detalles, result.getDetallesEspecificos());
        assertEquals(detalles, perfil.getDetallesEspecificos()); // Verificar entidad mutada
        
        verify(perfilRepository).findById(perfilId);
        verify(perfilRepository).save(perfil);
    }

    @Test
    @DisplayName("actualizarDetallesEspecificos - Perfil no encontrado lanza excepcion")
    void actualizarDetallesEspecificos_perfilNoEncontrado_lanzaExcepcion() {
        // Arrange
        Long perfilId = 99L;
        when(perfilRepository.findById(perfilId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, 
                () -> service.actualizarDetallesEspecificos(perfilId, "Detalles cualquiera"));
    }

}
