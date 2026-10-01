package com.oficioya.service.impl;

import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.OficioRepository;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.validator.IBusquedaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Pruebas del método buscar(CriteriosBusqueda) — RF-11 a RF-15 y RF-32 (Dev B). */
@ExtendWith(MockitoExtension.class)
class BusquedaTrabajadorServiceImplBuscarTest {

    @Mock private PerfilTrabajadorRepository repository;
    @Mock private PerfilTrabajadorEntityMapper mapper;
    @Mock private OficioRepository oficioRepository;
    @Mock private IBusquedaValidator busquedaValidator;

    @InjectMocks private BusquedaTrabajadorServiceImpl service;

    @Test
    @DisplayName("buscar - sin texto no consulta el catálogo y ordena por reputación por defecto")
    void buscar_sinTexto_ordenaPorReputacionSinConsultarCatalogo() {
        // Arrange
        CriteriosBusqueda criterios = CriteriosBusqueda.builder().zona("Chapinero").build();
        PerfilTrabajadorEntity entidad = PerfilTrabajadorEntity.builder().id(1L).build();
        PerfilTrabajador dominio = PerfilTrabajador.builder().id(1L).build();
        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(entidad));
        when(mapper.toDomain(entidad)).thenReturn(dominio);

        // Act
        List<PerfilTrabajador> resultado = service.buscar(criterios);

        // Assert
        assertEquals(List.of(dominio), resultado);
        ArgumentCaptor<Sort> sort = ArgumentCaptor.forClass(Sort.class);
        verify(repository).findAll(any(Specification.class), sort.capture());
        assertEquals(Sort.Direction.DESC, sort.getValue().getOrderFor("calificacionPromedio").getDirection());
        verify(oficioRepository, never()).findAll();
        verify(busquedaValidator).validarRangoTarifa(criterios);
        verify(busquedaValidator).validarFranjaSolicitada(criterios);
    }

    @Test
    @DisplayName("buscar - orden DISTANCIA (sin importar mayúsculas) ordena primero por zona")
    void buscar_ordenDistancia_ordenaPorZona() {
        // Arrange
        CriteriosBusqueda criterios = CriteriosBusqueda.builder().orden("distancia").build();
        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        // Act
        service.buscar(criterios);

        // Assert
        ArgumentCaptor<Sort> sort = ArgumentCaptor.forClass(Sort.class);
        verify(repository).findAll(any(Specification.class), sort.capture());
        assertEquals(Sort.Direction.ASC, sort.getValue().getOrderFor("zonaCobertura").getDirection());
    }

    @Test
    @DisplayName("buscar - texto libre consulta el catálogo de oficios y devuelve los resultados mapeados")
    void buscar_textoLibre_consultaCatalogo() {
        // Arrange
        CriteriosBusqueda criterios = CriteriosBusqueda.builder().texto("alguien que arregle una gotera").build();
        OficioEntity plomeria = OficioEntity.builder().id(1L).nombre("Plomería").categoria("Hogar").build();
        OficioEntity profesor = OficioEntity.builder().id(2L).nombre("Clases particulares").categoria("Educación").build();
        PerfilTrabajadorEntity entidad = PerfilTrabajadorEntity.builder().id(9L).build();
        PerfilTrabajador dominio = PerfilTrabajador.builder().id(9L).build();
        when(oficioRepository.findAll()).thenReturn(List.of(plomeria, profesor));
        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(entidad));
        when(mapper.toDomain(entidad)).thenReturn(dominio);

        // Act
        List<PerfilTrabajador> resultado = service.buscar(criterios);

        // Assert
        assertEquals(1, resultado.size());
        verify(oficioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscar - sin coincidencias devuelve lista vacía, no null")
    void buscar_sinCoincidencias_devuelveListaVacia() {
        // Arrange
        CriteriosBusqueda criterios = CriteriosBusqueda.builder().categoria("Mascotas").build();
        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        // Act
        List<PerfilTrabajador> resultado = service.buscar(criterios);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscar - rango de tarifa inválido propaga ReglaDeNegocioException y no consulta")
    void buscar_rangoTarifaInvalido_propagaReglaDeNegocio() {
        // Arrange
        CriteriosBusqueda criterios = CriteriosBusqueda.builder().build();
        doThrow(new ReglaDeNegocioException("Rango inválido")).when(busquedaValidator).validarRangoTarifa(criterios);

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> service.buscar(criterios));
        verify(repository, never()).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    @DisplayName("buscar - franja horaria inválida propaga ReglaDeNegocioException y no consulta")
    void buscar_franjaInvalida_propagaReglaDeNegocio() {
        // Arrange
        CriteriosBusqueda criterios = CriteriosBusqueda.builder().build();
        doThrow(new ReglaDeNegocioException("Franja inválida")).when(busquedaValidator).validarFranjaSolicitada(criterios);

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> service.buscar(criterios));
        verify(repository, never()).findAll(any(Specification.class), any(Sort.class));
    }
}
