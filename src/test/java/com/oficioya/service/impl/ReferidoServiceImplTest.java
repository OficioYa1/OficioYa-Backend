package com.oficioya.service.impl;

import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.persistence.entity.ReferidoEntity;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.ReferidoRepository;
import com.oficioya.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void registrarReferido_Exitoso() {
        UsuarioEntity referente = UsuarioEntity.builder().id(1L).correo("ref@mail.com").build();
        when(usuarioRepository.findByCorreo("ref@mail.com")).thenReturn(Optional.of(referente));
        when(usuarioRepository.findByCorreo("nuevo@mail.com")).thenReturn(Optional.empty());
        when(referidoRepository.findByCorreoReferido("nuevo@mail.com")).thenReturn(Optional.empty());

        referidoService.registrarReferido("ref@mail.com", "nuevo@mail.com");

        verify(referidoRepository).save(any(ReferidoEntity.class));
    }

    @Test
    void procesarRegistroReferido_AplicaEmbajador() {
        UsuarioEntity referente = UsuarioEntity.builder().id(1L).embajador(false).build();
        ReferidoEntity referido = ReferidoEntity.builder().referente(referente).build();

        when(referidoRepository.findByCorreoReferido("nuevo@mail.com")).thenReturn(Optional.of(referido));
        when(referidoRepository.countByReferenteIdAndCuentaCreadaTrue(1L)).thenReturn(5L);

        referidoService.procesarRegistroReferido("nuevo@mail.com");

        verify(referidoRepository).save(referido);
        verify(usuarioRepository).save(argThat(u -> u.isEmbajador()));
    }
}
