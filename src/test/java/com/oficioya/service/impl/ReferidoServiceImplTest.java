package com.oficioYa.service.impl;

import com.oficioYa.exception.EstadoInvalidoException;
import com.oficioYa.exception.UsuarioNoEncontradoException;
import com.oficioYa.persistence.entity.ReferidoEntity;
import com.oficioYa.persistence.entity.UsuarioEntity;
import com.oficioYa.repository.ReferidoRepository;
import com.oficioYa.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferidoServiceImplTest {

    @Mock
    private ReferidoRepository referidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReferidoServiceImpl referidoService;

    @Test
    @DisplayName("Debe registrar referido exitosamente cuando los datos son válidos")
    void registrarReferido_ConDatosValidos_DebeGuardarReferido() {
        // Arrange
        Long referenteId = 1L;
        String correoReferido = "nuevoamigo@oficioya.com";
        UsuarioEntity referente = UsuarioEntity.builder().id(referenteId).correo("referente@oficioya.com").build();

        when(usuarioRepository.findById(referenteId)).thenReturn(Optional.of(referente));
        when(usuarioRepository.findByCorreo(correoReferido)).thenReturn(Optional.empty());
        when(referidoRepository.findByCorreoReferido(correoReferido)).thenReturn(Optional.empty());

        // Act
        referidoService.registrarReferido(referenteId, correoReferido);

        // Assert
        ArgumentCaptor<ReferidoEntity> captor = ArgumentCaptor.forClass(ReferidoEntity.class);
        verify(referidoRepository).save(captor.capture());
        ReferidoEntity saved = captor.getValue();

        assertEquals(referente, saved.getReferente());
        assertEquals(correoReferido, saved.getCorreoReferido());
        assertFalse(saved.isCuentaCreada());
        assertNotNull(saved.getFechaInvitacion());
        verify(usuarioRepository).findById(referenteId);
    }

    @Test
    @DisplayName("Debe lanzar excepción al registrar referido si el referente no existe")
    void registrarReferido_ReferenteInexistente_DebeLanzarExcepcion() {
        // Arrange
        Long referenteId = 999L;
        String correoReferido = "amigo@oficioya.com";

        when(usuarioRepository.findById(referenteId)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> referidoService.registrarReferido(referenteId, correoReferido)
        );

        // Assert
        assertEquals("Referente no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(referenteId);
        verifyNoInteractions(referidoRepository);
    }

    @Test
    @DisplayName("Debe lanzar excepción al registrar referido si el correo ya tiene cuenta activa")
    void registrarReferido_CorreoYaRegistrado_DebeLanzarExcepcion() {
        // Arrange
        Long referenteId = 1L;
        String correoExistente = "existente@oficioya.com";
        UsuarioEntity referente = UsuarioEntity.builder().id(referenteId).build();
        UsuarioEntity usuarioExistente = UsuarioEntity.builder().id(2L).correo(correoExistente).build();

        when(usuarioRepository.findById(referenteId)).thenReturn(Optional.of(referente));
        when(usuarioRepository.findByCorreo(correoExistente)).thenReturn(Optional.of(usuarioExistente));

        // Act & Assert
        EstadoInvalidoException exception = assertThrows(
                EstadoInvalidoException.class,
                () -> referidoService.registrarReferido(referenteId, correoExistente)
        );

        // Assert
        assertEquals("El correo referido ya pertenece a una cuenta existente", exception.getMessage());
        verify(usuarioRepository).findById(referenteId);
        verify(usuarioRepository).findByCorreo(correoExistente);
        verify(referidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al registrar si el correo ya fue invitado previamente")
    void registrarReferido_CorreoYaInvitado_DebeLanzarExcepcion() {
        // Arrange
        Long referenteId = 1L;
        String correoInvitado = "ya_invitado@oficioya.com";
        UsuarioEntity referente = UsuarioEntity.builder().id(referenteId).build();
        ReferidoEntity invitacionPrevia = ReferidoEntity.builder().correoReferido(correoInvitado).build();

        when(usuarioRepository.findById(referenteId)).thenReturn(Optional.of(referente));
        when(usuarioRepository.findByCorreo(correoInvitado)).thenReturn(Optional.empty());
        when(referidoRepository.findByCorreoReferido(correoInvitado)).thenReturn(Optional.of(invitacionPrevia));

        // Act & Assert
        EstadoInvalidoException exception = assertThrows(
                EstadoInvalidoException.class,
                () -> referidoService.registrarReferido(referenteId, correoInvitado)
        );

        // Assert
        assertEquals("Este correo ya ha sido invitado previamente", exception.getMessage());
        verify(referidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("No debe realizar acciones si el correo del nuevo usuario no fue referido")
    void procesarRegistroReferido_CuandoNoFueReferido_NoDebeHacerNada() {
        // Arrange
        String correoNoReferido = "independiente@oficioya.com";
        when(referidoRepository.findByCorreoReferido(correoNoReferido)).thenReturn(Optional.empty());

        // Act
        referidoService.procesarRegistroReferido(correoNoReferido);

        // Assert
        verify(referidoRepository).findByCorreoReferido(correoNoReferido);
        verify(referidoRepository, never()).save(any());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Debe actualizar cuenta creada cuando nuevo usuario fue referido y total es menor a 5")
    void procesarRegistroReferido_CuandoReferidoExisteYMenorA5_NoDebePromoverEmbajador() {
        // Arrange
        String correo = "referido@oficioya.com";
        UsuarioEntity referente = UsuarioEntity.builder().id(1L).embajador(false).build();
        ReferidoEntity referido = ReferidoEntity.builder()
                .id(10L)
                .referente(referente)
                .correoReferido(correo)
                .cuentaCreada(false)
                .fechaInvitacion(LocalDateTime.now())
                .build();

        when(referidoRepository.findByCorreoReferido(correo)).thenReturn(Optional.of(referido));
        when(referidoRepository.countByReferenteIdAndCuentaCreadaTrue(1L)).thenReturn(4L);

        // Act
        referidoService.procesarRegistroReferido(correo);

        // Assert
        assertTrue(referido.isCuentaCreada());
        verify(referidoRepository).save(referido);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe promover a Embajador cuando el referente alcanza 5 o más referidos confirmados")
    void procesarRegistroReferido_CuandoAlcanzaCincoReferidos_DebePromoverAEmbajador() {
        // Arrange
        String correo = "quinto_referido@oficioya.com";
        UsuarioEntity referente = UsuarioEntity.builder().id(1L).embajador(false).build();
        ReferidoEntity referido = ReferidoEntity.builder()
                .id(10L)
                .referente(referente)
                .correoReferido(correo)
                .cuentaCreada(false)
                .fechaInvitacion(LocalDateTime.now())
                .build();

        when(referidoRepository.findByCorreoReferido(correo)).thenReturn(Optional.of(referido));
        when(referidoRepository.countByReferenteIdAndCuentaCreadaTrue(1L)).thenReturn(5L);

        // Act
        referidoService.procesarRegistroReferido(correo);

        // Assert
        assertTrue(referido.isCuentaCreada());
        assertTrue(referente.isEmbajador());
        verify(referidoRepository).save(referido);
        verify(usuarioRepository).save(referente);
    }

    @Test
    @DisplayName("No debe volver a guardar usuario si ya tenía la distinción de Embajador")
    void procesarRegistroReferido_CuandoYaEraEmbajador_NoDebeVolverAGuardarUsuario() {
        // Arrange
        String correo = "sexto_referido@oficioya.com";
        UsuarioEntity referente = UsuarioEntity.builder().id(1L).embajador(true).build();
        ReferidoEntity referido = ReferidoEntity.builder()
                .id(11L)
                .referente(referente)
                .correoReferido(correo)
                .cuentaCreada(false)
                .fechaInvitacion(LocalDateTime.now())
                .build();

        when(referidoRepository.findByCorreoReferido(correo)).thenReturn(Optional.of(referido));
        when(referidoRepository.countByReferenteIdAndCuentaCreadaTrue(1L)).thenReturn(6L);

        // Act
        referidoService.procesarRegistroReferido(correo);

        // Assert
        assertTrue(referido.isCuentaCreada());
        verify(referidoRepository).save(referido);
        verify(usuarioRepository, never()).save(any());
    }
}
