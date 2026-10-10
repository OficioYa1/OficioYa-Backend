package com.oficioYa.service;

import com.oficioYa.mapper.OficioMapper;
import com.oficioYa.model.dto.response.OficioResponseDTO;
import com.oficioYa.persistence.entity.OficioEntity;
import com.oficioYa.repository.OficioRepository;
import com.oficioYa.service.impl.OficioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OficioServiceImplTest {

    @Mock
    private OficioRepository oficioRepository;

    @Mock
    private OficioMapper oficioMapper;

    @InjectMocks
    private OficioServiceImpl oficioService;

    // =========================================================================
    // TESTS PARA RF-77: LISTAR OFICIOS BASE
    // =========================================================================

    @Test
    @DisplayName("listarOficiosActivos - Flujo Exitoso - Retorna oficios activos")
    void listarOficiosActivos_flujoExitoso_retornaOficios() {
        // Arrange
        OficioEntity oficioActivo1 = new OficioEntity();
        oficioActivo1.setId(1L);
        oficioActivo1.setNombre("Plomería");
        oficioActivo1.setActivo(true);

        OficioEntity oficioInactivo = new OficioEntity();
        oficioInactivo.setId(2L);
        oficioInactivo.setNombre("Electricidad Obsoleta");
        oficioInactivo.setActivo(false);

        OficioEntity oficioActivo2 = new OficioEntity();
        oficioActivo2.setId(3L);
        oficioActivo2.setNombre("Carpintería");
        oficioActivo2.setActivo(true);

        List<OficioEntity> todosLosOficios = Arrays.asList(oficioActivo1, oficioInactivo, oficioActivo2);

        OficioResponseDTO dto1 = OficioResponseDTO.builder().id(1L).nombre("Plomería").build();
        OficioResponseDTO dto2 = OficioResponseDTO.builder().id(3L).nombre("Carpintería").build();
        List<OficioResponseDTO> listaMapeada = Arrays.asList(dto1, dto2);

        when(oficioRepository.findAll()).thenReturn(todosLosOficios);
        when(oficioMapper.toDtoList(anyList())).thenReturn(listaMapeada);

        // Act
        List<OficioResponseDTO> resultado = oficioService.listarOficiosActivos();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Plomería", resultado.get(0).getNombre());
        verify(oficioRepository, times(1)).findAll();
        verify(oficioMapper, times(1)).toDtoList(anyList());
    }
}
