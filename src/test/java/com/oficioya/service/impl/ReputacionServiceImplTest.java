package com.oficioYa.service.impl;

import com.oficioYa.exception.RecursoNoEncontradoException;
import com.oficioYa.exception.EstadoInvalidoException;
import com.oficioYa.model.dto.request.ReporteResenaRequestDTO;
import com.oficioYa.model.dto.request.ResenaRequestDTO;
import com.oficioYa.model.dto.response.ReputacionPorOficioDTO;
import com.oficioYa.model.dto.response.ResenaResponseDTO;
import com.oficioYa.persistence.entity.*;
import com.oficioYa.repository.PerfilTrabajadorRepository;
import com.oficioYa.repository.ResenaRepository;
import com.oficioYa.repository.SolicitudRepository;
import com.oficioYa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class ReputacionServiceImplTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilTrabajadorRepository perfilRepository;

    @InjectMocks
    private ReputacionServiceImpl reputacionService;

    private SolicitudEntity solicitudFinalizada;
    private UsuarioEntity contratante;
    private UsuarioEntity trabajador;
    private PerfilTrabajadorEntity perfilTrabajador;
    private OficioEntity oficio;
    private ResenaRequestDTO resenaRequest;

    @BeforeEach
    void setUp() {
        contratante = UsuarioEntity.builder().id(1L).nombre("Juan").rol(RolUsuario.CONTRATANTE).build();
        trabajador = UsuarioEntity.builder().id(2L).nombre("Pedro").rol(RolUsuario.TRABAJADOR).build();
        oficio = OficioEntity.builder().id(1L).nombre("Plomero").build();

        solicitudFinalizada = SolicitudEntity.builder()
                .id(1L)
                .contratante(contratante)
                .trabajador(trabajador)
                .estado(EstadoSolicitud.COMPLETADA)
                .build();

        perfilTrabajador = PerfilTrabajadorEntity.builder()
                .id(1L)
                .usuario(trabajador)
                .oficioPrincipal(oficio)
                .calificacionPromedio(0.0)
                .disponibleAhora(true)
                .build();

        resenaRequest = new ResenaRequestDTO();
        resenaRequest.setEstrellas(4);
        resenaRequest.setComentario("Buen trabajo");
    }

    @Test
    @DisplayName("Calificar trabajador - Exito")
    void calificarTrabajador_Exito() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudFinalizada));
        when(perfilRepository.findByUsuarioId(2L)).thenReturn(Optional.of(perfilTrabajador));
        when(resenaRepository.save(any(ResenaEntity.class))).thenAnswer(i -> {
            ResenaEntity resena = (ResenaEntity) i.getArguments()[0];
            resena.setId(10L);
            return resena;
        });
        when(resenaRepository.calcularPromedioPorReceptor(2L)).thenReturn(Optional.of(4.0));
        when(resenaRepository.countByReceptorIdAndReportadaFalse(2L)).thenReturn(1L);

        ResenaResponseDTO response = reputacionService.calificarTrabajador(1L, 1L, resenaRequest);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(4, response.getEstrellas());
        verify(perfilRepository, atLeastOnce()).save(perfilTrabajador);
        assertEquals(4.0, perfilTrabajador.getCalificacionPromedio());
    }

    @Test
    @DisplayName("Calificar trabajador - Error: Solicitud no finalizada")
    void calificarTrabajador_ErrorNoFinalizada() {
        solicitudFinalizada.setEstado(EstadoSolicitud.CREADA);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudFinalizada));

        assertThrows(EstadoInvalidoException.class, () -> 
            reputacionService.calificarTrabajador(1L, 1L, resenaRequest)
        );
    }

    @Test
    @DisplayName("Calificar contratante - Exito")
    void calificarContratante_Exito() {
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudFinalizada));
        when(perfilRepository.findByUsuarioId(2L)).thenReturn(Optional.of(perfilTrabajador));
        when(resenaRepository.save(any(ResenaEntity.class))).thenAnswer(i -> {
            ResenaEntity resena = (ResenaEntity) i.getArguments()[0];
            resena.setId(11L);
            return resena;
        });
        when(resenaRepository.calcularPromedioPorReceptor(1L)).thenReturn(Optional.of(5.0));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(contratante));

        ResenaResponseDTO response = reputacionService.calificarContratante(1L, 2L, resenaRequest);

        assertNotNull(response);
        assertEquals(11L, response.getId());
        verify(usuarioRepository).save(contratante);
        assertEquals(5.0, contratante.getCalificacionPromedio());
    }

    @Test
    @DisplayName("Calificar trabajador - Auto-pausar por baja reputación")
    void calificarTrabajador_BajaReputacionPausa() {
        resenaRequest.setEstrellas(1);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudFinalizada));
        when(perfilRepository.findByUsuarioId(2L)).thenReturn(Optional.of(perfilTrabajador));
        when(resenaRepository.save(any(ResenaEntity.class))).thenAnswer(i -> {
            ResenaEntity resena = (ResenaEntity) i.getArguments()[0];
            resena.setId(10L);
            return resena;
        });
        when(resenaRepository.calcularPromedioPorReceptor(2L)).thenReturn(Optional.of(2.5)); // Promedio < 3.0
        when(resenaRepository.countByReceptorIdAndReportadaFalse(2L)).thenReturn(3L); // 3 reseñas mínimo

        reputacionService.calificarTrabajador(1L, 1L, resenaRequest);

        assertFalse(perfilTrabajador.isDisponibleAhora()); // RF-39 Auto pause
        verify(perfilRepository, atLeastOnce()).save(perfilTrabajador);
    }

    @Test
    @DisplayName("Reportar reseña - Exito")
    void reportarResena_Exito() {
        ResenaEntity resena = ResenaEntity.builder()
                .id(1L)
                .receptor(trabajador)
                .reportada(false)
                .build();
                
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(trabajador));
        when(resenaRepository.calcularPromedioPorReceptor(2L)).thenReturn(Optional.of(4.5));
        when(perfilRepository.findByUsuarioId(2L)).thenReturn(Optional.of(perfilTrabajador));

        ReporteResenaRequestDTO req = new ReporteResenaRequestDTO();
        req.setMotivo("Ofensivo");
        
        reputacionService.reportarResena(1L, req);

        assertTrue(resena.isReportada());
        verify(resenaRepository).save(resena);
        verify(perfilRepository).save(perfilTrabajador); // Se recalcula el promedio sin esta reseña
    }

    @Test
    @DisplayName("Obtener reputación por oficio - Exito")
    void obtenerReputacionPorOficio_Exito() {
        when(resenaRepository.calcularPromedioPorReceptorYOficio(2L, 1L)).thenReturn(Optional.of(4.8));
        when(resenaRepository.countByReceptorIdAndReportadaFalse(2L)).thenReturn(10L);

        ReputacionPorOficioDTO response = reputacionService.obtenerReputacionPorOficio(2L, 1L);

        assertEquals(4.8, response.getCalificacionPromedio());
        assertEquals(10L, response.getTotalResenas());
    }
}
