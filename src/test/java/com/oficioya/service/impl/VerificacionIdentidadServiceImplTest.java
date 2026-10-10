package com.oficioYa.service.impl;

import com.oficioYa.exception.RecursoNoEncontradoException;
import com.oficioYa.model.domain.EstadoVerificacion;
import com.oficioYa.persistence.entity.PerfilTrabajadorEntity;
import com.oficioYa.repository.PerfilTrabajadorRepository;
import com.oficioYa.service.IStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificacionIdentidadServiceImplTest {

    @Mock
    private PerfilTrabajadorRepository perfilTrabajadorRepository;

    @Mock
    private IStorageService storageService;

    @InjectMocks
    private VerificacionIdentidadServiceImpl verificacionService;

    private PerfilTrabajadorEntity perfil;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        perfil = new PerfilTrabajadorEntity();
        perfil.setEstadoVerificacion(EstadoVerificacion.NO_VERIFICADO);
        com.oficioYa.persistence.entity.UsuarioEntity usuario = new com.oficioYa.persistence.entity.UsuarioEntity();
        usuario.setId(1L);
        perfil.setUsuario(usuario);
        
        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
    }

    @Test
    void subirDocumentosIdentidad_Success() {
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfil));
        when(storageService.guardarImagen(any(MultipartFile.class))).thenReturn("url_frente", "url_reverso");

        String res = verificacionService.subirDocumentosIdentidad(1L, mockFile, mockFile);

        assertEquals("url_frente;url_reverso", res);
        assertEquals("url_frente;url_reverso", perfil.getDocumentosIdentidadUrl());
        verify(perfilTrabajadorRepository, times(1)).save(perfil);
    }

    @Test
    void subirCertificadoAntecedentes_Success() {
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfil));
        when(storageService.guardarImagen(any(MultipartFile.class))).thenReturn("url_cert");

        String res = verificacionService.subirCertificadoAntecedentes(1L, mockFile);

        assertEquals("url_cert", res);
        assertEquals("url_cert", perfil.getCertificadoAntecedentesUrl());
        verify(perfilTrabajadorRepository, times(1)).save(perfil);
    }

    @Test
    void subirFotoPerfilVerificada_Success() {
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfil));
        when(storageService.guardarImagen(any(MultipartFile.class))).thenReturn("url_foto");

        String res = verificacionService.subirFotoPerfilVerificada(1L, mockFile);

        assertEquals("url_foto", res);
        assertEquals("url_foto", perfil.getFotoPerfilVerificadaUrl());
        verify(perfilTrabajadorRepository, times(1)).save(perfil);
    }

    @Test
    void verificarTransicionEstado_ToPendienteRevision() {
        perfil.setDocumentosIdentidadUrl("doc");
        perfil.setCertificadoAntecedentesUrl("cert");
        
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfil));
        when(storageService.guardarImagen(any(MultipartFile.class))).thenReturn("url_foto");

        verificacionService.subirFotoPerfilVerificada(1L, mockFile);

        assertEquals(EstadoVerificacion.PENDIENTE_REVISION, perfil.getEstadoVerificacion());
        verify(perfilTrabajadorRepository, times(1)).save(perfil);
    }

    @Test
    void obtenerEstadoVerificacion_Success() {
        perfil.setEstadoVerificacion(EstadoVerificacion.APROBADO);
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfil));

        EstadoVerificacion estado = verificacionService.obtenerEstadoVerificacion(1L);

        assertEquals(EstadoVerificacion.APROBADO, estado);
    }

    @Test
    void actualizarEstadoVerificacion_Success() {
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(perfil));

        verificacionService.actualizarEstadoVerificacion(1L, EstadoVerificacion.RECHAZADO);

        assertEquals(EstadoVerificacion.RECHAZADO, perfil.getEstadoVerificacion());
        verify(perfilTrabajadorRepository, times(1)).save(perfil);
    }

    @Test
    void obtenerPerfil_ThrowsException() {
        when(perfilTrabajadorRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            verificacionService.obtenerEstadoVerificacion(1L);
        });
    }
}
