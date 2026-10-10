package com.oficioYa.service.impl;

import com.oficioYa.model.domain.CriteriosBusqueda;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.persistence.entity.FranjaDisponibilidadEmbeddable;
import com.oficioYa.persistence.entity.OficioEntity;
import com.oficioYa.persistence.entity.PerfilTrabajadorEntity;
import com.oficioYa.persistence.entity.RolUsuario;
import com.oficioYa.persistence.entity.UsuarioEntity;
import com.oficioYa.repository.OficioRepository;
import com.oficioYa.repository.PerfilTrabajadorRepository;
import com.oficioYa.repository.UsuarioRepository;
import com.oficioYa.service.IBusquedaTrabajadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integración de los filtros de búsqueda contra H2 real (Specifications JPA).
 * Los oficios llevan el sufijo "(test)" para no chocar con el catálogo que cargue el DataSeeder.
 */
@SpringBootTest
@Transactional
class BusquedaTrabajadoresIntegrationTest {

    @Autowired private IBusquedaTrabajadorService service;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private OficioRepository oficioRepository;
    @Autowired private PerfilTrabajadorRepository perfilRepository;

    private OficioEntity plomeria;
    private OficioEntity electricidad;
    private OficioEntity clases;
    private PerfilTrabajadorEntity plomero;
    private PerfilTrabajadorEntity electricista;
    private PerfilTrabajadorEntity profesor;

    @BeforeEach
    void setUp() {
        plomeria = oficioRepository.save(OficioEntity.builder().nombre("Plomería (test)").categoria("Hogar").build());
        electricidad = oficioRepository.save(OficioEntity.builder().nombre("Electricidad (test)").categoria("Hogar").build());
        clases = oficioRepository.save(OficioEntity.builder().nombre("Clases de matemáticas (test)").categoria("Educación").build());

        plomero = perfil("plomero", plomeria, "Chapinero, Usaquén", "30000", 4.8, 12, true,
                franja(DayOfWeek.MONDAY, 8, 17), "Arreglo goteras y fugas");
        electricista = perfil("electricista", electricidad, "Suba", "50000", 4.0, 5, false,
                franja(DayOfWeek.TUESDAY, 9, 12), "Instalaciones eléctricas");
        profesor = perfil("profesor", clases, "Chapinero", "20000", 3.5, 2, false,
                franja(DayOfWeek.MONDAY, 14, 18), "Refuerzo escolar");
    }

    private FranjaDisponibilidadEmbeddable franja(DayOfWeek dia, int desde, int hasta) {
        return FranjaDisponibilidadEmbeddable.builder().dia(dia)
                .horaInicio(LocalTime.of(desde, 0)).horaFin(LocalTime.of(hasta, 0)).build();
    }

    private PerfilTrabajadorEntity perfil(String clave, OficioEntity oficio, String zona, String tarifa,
                                          double calificacion, int trabajos, boolean ahora,
                                          FranjaDisponibilidadEmbeddable franja, String descripcion) {
        UsuarioEntity usuario = usuarioRepository.save(UsuarioEntity.builder()
                .correo(clave + "@busqueda-test.com").telefono("3" + Math.abs(clave.hashCode()))
                .contrasena("x").nombre(clave).apellido("Test").rol(RolUsuario.TRABAJADOR)
                .correoVerificado(true).build());
        return perfilRepository.saveAndFlush(PerfilTrabajadorEntity.builder()
                .usuario(usuario).zonaCobertura(zona).tarifaPorHora(new BigDecimal(tarifa))
                .calificacionPromedio(calificacion).trabajosCompletados(trabajos).disponibleAhora(ahora)
                .descripcion(descripcion)
                .oficios(new ArrayList<>(List.of(oficio)))
                .disponibilidadSemanal(new ArrayList<>(List.of(franja)))
                .build());
    }

    private List<Long> ids(List<PerfilTrabajador> resultado) {
        return resultado.stream().map(PerfilTrabajador::getId).toList();
    }

    @Test
    @DisplayName("RF-12 - por categoría devuelve los trabajadores de esa categoría, sin duplicados")
    void buscar_porCategoria_devuelveSoloEsaCategoria() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder().categoria("hogar").build());

        // Assert
        assertEquals(List.of(plomero.getId(), electricista.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-12 - por oficio devuelve solo quienes lo ofrecen")
    void buscar_porOficio_devuelveSoloQuienesLoOfrecen() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder().oficioId(electricidad.getId()).build());

        // Assert
        assertEquals(List.of(electricista.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-13 - por zona devuelve quienes la cubren (sin distinguir mayúsculas)")
    void buscar_porZona_devuelveQuienesLaCubren() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder().zona("CHAPINERO").build());

        // Assert
        assertEquals(List.of(plomero.getId(), profesor.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-14 - por rango de tarifa excluye a quienes cobran fuera del rango")
    void buscar_porRangoTarifa_excluyeFueraDelRango() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder()
                .tarifaMin(new BigDecimal("25000")).tarifaMax(new BigDecimal("40000")).build());

        // Assert
        assertEquals(List.of(plomero.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-15 - por día y hora devuelve a quien tiene una franja que cubre el rango")
    void buscar_porDiaYHora_devuelveQuienCubreLaFranja() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder()
                .dia(DayOfWeek.MONDAY).horaDesde(LocalTime.of(9, 0)).horaHasta(LocalTime.of(11, 0)).build());

        // Assert
        assertEquals(List.of(plomero.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-15 - hora fuera de las franjas devuelve lista vacía")
    void buscar_horaFueraDeFranjas_devuelveVacio() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder()
                .dia(DayOfWeek.MONDAY).horaDesde(LocalTime.of(20, 0)).build());

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("RF-32 - solo disponibles ahora devuelve únicamente a quien lo tiene activo")
    void buscar_soloDisponiblesAhora_devuelveSoloActivos() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder().soloDisponiblesAhora(true).build());

        // Assert
        assertEquals(List.of(plomero.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-11 - 'alguien que arregle una gotera' encuentra al plomero")
    void buscar_textoLibreGotera_encuentraAlPlomero() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(
                CriteriosBusqueda.builder().texto("alguien que arregle una gotera").build());

        // Assert
        assertEquals(List.of(plomero.getId()), ids(resultado));
    }

    @Test
    @DisplayName("RF-11 - texto sin coincidencias devuelve lista vacía")
    void buscar_textoSinCoincidencias_devuelveVacio() {
        // Act
        List<PerfilTrabajador> resultado = service.buscar(CriteriosBusqueda.builder().texto("necesito un astronauta").build());

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Combinación - categoría + calificación mínima + orden por reputación")
    void buscar_filtrosCombinados_aplicaTodosConOrdenDeReputacion() {
        // Act
        List<PerfilTrabajador> todosHogar = service.buscar(CriteriosBusqueda.builder().categoria("Hogar").orden("REPUTACION").build());
        List<PerfilTrabajador> exigentes = service.buscar(CriteriosBusqueda.builder()
                .categoria("Hogar").calificacionMinima(4.5).build());

        // Assert
        assertEquals(List.of(plomero.getId(), electricista.getId()), ids(todosHogar));
        assertEquals(List.of(plomero.getId()), ids(exigentes));
    }

    @Test
    @DisplayName("Sin filtros devuelve todos los perfiles ordenados por reputación")
    void buscar_sinFiltros_devuelveTodosPorReputacion() {
        // Act
        List<Long> ids = ids(service.buscar(CriteriosBusqueda.builder().build()));

        // Assert
        assertTrue(ids.containsAll(List.of(plomero.getId(), electricista.getId(), profesor.getId())));
        assertTrue(ids.indexOf(plomero.getId()) < ids.indexOf(electricista.getId()));
        assertTrue(ids.indexOf(electricista.getId()) < ids.indexOf(profesor.getId()));
    }
}
