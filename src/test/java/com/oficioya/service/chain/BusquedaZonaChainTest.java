package com.oficioya.service.chain;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusquedaZonaChainTest {

    @Mock
    private PerfilTrabajadorRepository repository;

    private BarrioExactoHandler barrioExactoHandler;
    private BarriosCercanosHandler barriosCercanosHandler;
    private CiudadHandler ciudadHandler;
    private BusquedaZonaChainService chainService;

    @BeforeEach
    void setUp() {
        // Arrange
        barrioExactoHandler = new BarrioExactoHandler();
        barriosCercanosHandler = new BarriosCercanosHandler();
        ciudadHandler = new CiudadHandler();
        chainService = new BusquedaZonaChainService(barrioExactoHandler, barriosCercanosHandler, ciudadHandler);
        chainService.ensamblarCadena();
    }

    @Test
    @DisplayName("Debe resolver en Eslabón 1 (Barrio Exacto) cuando existe coincidencia exacta")
    void debeResolverEnBarrioExacto() {
        // Arrange
        String zona = "Chapinero";
        PerfilTrabajadorEntity p1 = new PerfilTrabajadorEntity();
        p1.setId(1L);
        p1.setZonaCobertura("Chapinero");

        when(repository.findAll()).thenReturn(List.of(p1));

        // Act
        List<PerfilTrabajadorEntity> resultado = chainService.resolverBusquedaPorZona(zona, repository);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Chapinero", resultado.get(0).getZonaCobertura());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe delegar a Eslabón 2 (Barrios Cercanos) cuando no hay coincidencia exacta")
    void debeDelegarABarriosCercanos() {
        // Arrange
        String zona = "Chapinero Alto";
        PerfilTrabajadorEntity p1 = new PerfilTrabajadorEntity();
        p1.setId(2L);
        p1.setZonaCobertura("Chapinero"); // Coincidencia parcial/cercana

        when(repository.findAll()).thenReturn(List.of(p1));

        // Act
        List<PerfilTrabajadorEntity> resultado = chainService.resolverBusquedaPorZona(zona, repository);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getId());
    }

    @Test
    @DisplayName("Debe delegar a Eslabón 3 (Ciudad Completa) cuando no hay coincidencia ni exacta ni cercana")
    void debeDelegarACiudadCompleta() {
        // Arrange
        String zona = "Suba";
        PerfilTrabajadorEntity p1 = new PerfilTrabajadorEntity();
        p1.setId(3L);
        p1.setZonaCobertura("Usaquén");

        when(repository.findAll()).thenReturn(List.of(p1));

        // Act
        List<PerfilTrabajadorEntity> resultado = chainService.resolverBusquedaPorZona(zona, repository);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getId());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si la zona proporcionada es nula o en blanco")
    void debeRetornarListaVaciaSiZonaEsInvalida() {
        // Arrange
        String zonaNula = null;
        String zonaVacia = "   ";

        // Act
        List<PerfilTrabajadorEntity> resNulo = chainService.resolverBusquedaPorZona(zonaNula, repository);
        List<PerfilTrabajadorEntity> resVacio = chainService.resolverBusquedaPorZona(zonaVacia, repository);

        // Assert
        assertTrue(resNulo.isEmpty());
        assertTrue(resVacio.isEmpty());
        verifyNoInteractions(repository);
    }
}
