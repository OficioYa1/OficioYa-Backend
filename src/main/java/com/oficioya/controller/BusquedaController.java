package com.oficioya.controller;

import com.oficioya.controller.docs.BusquedaApi;
import com.oficioya.mapper.BusquedaMapper;
import com.oficioya.mapper.PerfilTrabajadorMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.FiltroBusquedaDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.service.IBusquedaTrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/busqueda/trabajadores")
@RequiredArgsConstructor
public class BusquedaController implements BusquedaApi {

    private final IBusquedaTrabajadorService busquedaService;
    private final PerfilTrabajadorMapper mapper;
    private final BusquedaMapper busquedaMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<PerfilTrabajadorResponseDTO>> buscarTrabajadores(
            @ParameterObject @Valid FiltroBusquedaDTO filtroBusquedaDTO) {
        List<PerfilTrabajador> trabajadores = busquedaService.buscar(busquedaMapper.toCriterios(filtroBusquedaDTO));
        return ResponseEntity.ok(trabajadores.stream().map(mapper::toResponse).toList());
    }
}
