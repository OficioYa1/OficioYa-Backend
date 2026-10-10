package com.oficioya.service.impl;

import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.model.dto.request.ReporteRequestDTO;
import com.oficioya.persistence.entity.ReporteEntity;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.ReporteRepository;
import com.oficioya.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeracionServiceImplTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ModeracionServiceImpl moderacionService;

    private UsuarioEntity reportador;
    private UsuarioEntity reportado;

    @BeforeEach
    void setUp() {
        reportador = UsuarioEntity.builder().id(1L).correo("rep@mail.com").rol(RolUsuario.CONTRATANTE).build();
        reportado = UsuarioEntity.builder().id(2L).correo("bad@mail.com").rol(RolUsuario.TRABAJADOR).activo(true).build();
    }

    @Test
    void reportarUsuario_creaReporteExitosamente() {
        ReporteRequestDTO request = new ReporteRequestDTO(2L, "Acoso", "Me insultó");
        when(usuarioRepository.findByCorreo("rep@mail.com")).thenReturn(Optional.of(reportador));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(reportado));
        
        ReporteEntity savedEntity = ReporteEntity.builder().id(1L).reportador(reportador).reportado(reportado).estado(com.oficioya.persistence.entity.EstadoReporte.PENDIENTE).fechaCreacion(java.time.LocalDateTime.now()).build();
        when(reporteRepository.save(any(ReporteEntity.class))).thenReturn(savedEntity);

        var res = moderacionService.reportarUsuario("rep@mail.com", request);

        assertNotNull(res);
        verify(reporteRepository).save(any(ReporteEntity.class));
    }
}
