package com.oficioYa.service.impl;

import com.oficioYa.mapper.PerfilTrabajadorEntityMapper;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.persistence.entity.PerfilTrabajadorEntity;
import com.oficioYa.repository.PerfilTrabajadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BusquedaTrabajadorServiceImplTest {

    @Mock
    private PerfilTrabajadorRepository repository;

    @Mock
    private PerfilTrabajadorEntityMapper mapper;
    
    @Mock
    private com.oficioYa.repository.OficioRepository oficioRepository;
    
    @Mock
    private com.oficioYa.validator.IBusquedaValidator busquedaValidator;

    private BusquedaTrabajadorServiceImpl service;

    private PerfilTrabajador p1, p2, p3, p4, p5;
    private PerfilTrabajadorEntity e1, e2, e3, e4, e5;

    @BeforeEach
    void setUp() {
        service = new BusquedaTrabajadorServiceImpl(
            repository, mapper, oficioRepository, busquedaValidator,
            Arrays.asList(
                new com.oficioYa.service.strategy.impl.BusquedaPorMeritoStrategy(),
                new com.oficioYa.service.strategy.impl.BusquedaPorDistanciaStrategy(new com.oficioYa.adapter.impl.MockMapboxAdapter())
            )
        );
        // Arrange general
        p1 = new PerfilTrabajador(); p1.setZonaCobertura("Norte"); p1.setCalificacionPromedio(5.0); p1.setTrabajosCompletados(10);
        p2 = new PerfilTrabajador(); p2.setZonaCobertura("Norte"); p2.setCalificacionPromedio(5.0); p2.setTrabajosCompletados(2);
        p3 = new PerfilTrabajador(); p3.setZonaCobertura("Norte"); p3.setCalificacionPromedio(4.0); p3.setTrabajosCompletados(20);
        p4 = new PerfilTrabajador(); p4.setZonaCobertura("Sur"); p4.setCalificacionPromedio(4.8); p4.setTrabajosCompletados(5);
        p5 = new PerfilTrabajador(); p5.setZonaCobertura("Norte"); p5.setCalificacionPromedio(null); p5.setTrabajosCompletados(0);
        e1 = new PerfilTrabajadorEntity(); e1.setId(1L);
        e2 = new PerfilTrabajadorEntity(); e2.setId(2L);
        e3 = new PerfilTrabajadorEntity(); e3.setId(3L);
        e4 = new PerfilTrabajadorEntity(); e4.setId(4L);
        e5 = new PerfilTrabajadorEntity(); e5.setId(5L);
    }

    private void mockRepositoryAndMapper() {
        lenient().when(repository.findByZonaCoberturaContaining(any())).thenAnswer(i -> {
            String z = i.getArgument(0);
            if ("Sur".equals(z)) return java.util.Collections.singletonList(e4);
            if ("Oeste".equals(z)) return java.util.Collections.emptyList();
            if ("Norte".equals(z)) return java.util.Arrays.asList(e1, e2, e3, e5);
            return java.util.Arrays.asList(e1, e2, e3, e4, e5); // Por defecto retorna todo (null, "", "A", etc)
        });
        lenient().when(mapper.toDomain(e1)).thenReturn(p1);
        lenient().when(mapper.toDomain(e2)).thenReturn(p2);
        lenient().when(mapper.toDomain(e3)).thenReturn(p3);
        lenient().when(mapper.toDomain(e4)).thenReturn(p4);
        lenient().when(mapper.toDomain(e5)).thenReturn(p5);
    }

    // --- RF-16: Filtrar calificacion ---
    @Test
    @DisplayName("RF-16 Test 1: Filtra correctamente calificacion minima 4.5")
    void rf16_test1_filtrarCalificacionMayorA45() {
        // Arrange
        mockRepositoryAndMapper();
        // Act
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, 4.5, "DISTANCIA");
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(p1) && result.contains(p2));
    }

    @Test
    @DisplayName("RF-16 Test 2: Excluye calificaciones menores al minimo")
    void rf16_test2_excluyeMenores() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, 4.5, "DISTANCIA");
        assertFalse(result.contains(p3)); // p3 tiene 4.0
    }

    @Test
    @DisplayName("RF-16 Test 3: Si minimo es null, no filtra por calificacion")
    void rf16_test3_minimoNullNoFiltra() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "DISTANCIA");
        assertEquals(4, result.size()); // p1, p2, p3, p5 (p4 es Sur)
    }

    @Test
    @DisplayName("RF-16 Test 4: Calificacion minima de 5.0 devuelve solo al mejor")
    void rf16_test4_minimo5() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, 5.0, "DISTANCIA");
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("RF-16 Test 5: Ignora trabajadores con calificacion nula si hay filtro")
    void rf16_test5_ignoraNullRating() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, 1.0, "DISTANCIA");
        assertFalse(result.contains(p5)); // p5 tiene null rating
    }

    // --- RF-17: Ordenar por distancia (filtro zonal) y RF-22 ---
    @Test
    @DisplayName("RF-17 Test 1: Muestra los de la misma zona de cobertura")
    void rf17_test1_mismaZona() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Sur", 1L, null, "DISTANCIA");
        assertEquals(1, result.size());
        assertEquals(p4, result.get(0));
    }

    @Test
    @DisplayName("RF-17 Test 2: Omite a los que no coinciden con la zona")
    void rf17_test2_omiteOtraZona() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "DISTANCIA");
        assertFalse(result.contains(p4));
    }

    @Test
    @DisplayName("RF-17 Test 3: Soporta coincidencias parciales de zona")
    void rf17_test3_coincidenciaParcial() {
        // Arrange
        p1.setZonaCobertura("Centro-Norte");
        mockRepositoryAndMapper();
        // Act
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "DISTANCIA");
        // Assert
        assertTrue(result.contains(p1));
    }

    @Test
    @DisplayName("RF-17 Test 4: Devuelve lista vacia si no hay nadie en la zona")
    void rf17_test4_listaVacia() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Oeste", 1L, null, "DISTANCIA");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("RF-17 Test 5: Ordena alfabeticamente por defecto")
    void rf17_test5_ordenPorDefecto() {
        p1.setZonaCobertura("B");
        p2.setZonaCobertura("A");
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("", 1L, null, "DISTANCIA");
        assertEquals(p2, result.get(0)); // A antes que B
    }

    // --- RF-18: Ordenar reputacion ---
    @Test
    @DisplayName("RF-18 Test 1: Ordena descendentemente por calificacion")
    void rf18_test1_ordenDescendente() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "REPUTACION");
        assertEquals(5.0, result.get(0).getCalificacionPromedio());
    }

    @Test
    @DisplayName("RF-18 Test 2: Desempata por cantidad de trabajos completados")
    void rf18_test2_desempateTrabajos() {
        mockRepositoryAndMapper();
        // p1 (10 trab) y p2 (2 trab) tienen 5.0
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "REPUTACION");
        assertEquals(p1, result.get(0));
        assertEquals(p2, result.get(1));
    }

    @Test
    @DisplayName("RF-18 Test 3: Trabajadores con calificacion null van al final")
    void rf18_test3_nullAlFinal() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "REPUTACION");
        assertEquals(p5, result.get(result.size()-1));
    }

    @Test
    @DisplayName("RF-18 Test 4: Insensible a mayusculas/minusculas en parametro")
    void rf18_test4_caseInsensitive() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "reputacion");
        assertEquals(p1, result.get(0));
    }

    @Test
    @DisplayName("RF-18 Test 5: Lista resultante mantiene tamano original tras orden")
    void rf18_test5_mismoTamano() {
        mockRepositoryAndMapper();
        List<PerfilTrabajador> result = service.buscarTrabajadores("Norte", 1L, null, "REPUTACION");
        assertEquals(4, result.size());
    }
}
