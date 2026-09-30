package com.oficioya.controller;

import com.oficioya.controller.docs.BusquedaApi;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.FiltroBusquedaDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.service.IBusquedaTrabajadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/busqueda/trabajadores")
@RequiredArgsConstructor
public class BusquedaController implements BusquedaApi {

    private final IBusquedaTrabajadorService busquedaService;
    private final PerfilTrabajadorMapper mapper;

    @Override
    @GetMapping
    public ResponseEntity<List<PerfilTrabajadorResponseDTO>> buscarTrabajadores(@org.springdoc.core.annotations.ParameterObject FiltroBusquedaDTO filtroBusquedaDTO) {
        List<PerfilTrabajador> trabajadores = busquedaService.buscarTrabajadores(
                filtroBusquedaDTO.getZona(),
                filtroBusquedaDTO.getOficioId(),
                filtroBusquedaDTO.getCalificacionMinima(),
                filtroBusquedaDTO.getOrden()
        );

        List<PerfilTrabajadorResponseDTO> response = trabajadores.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
