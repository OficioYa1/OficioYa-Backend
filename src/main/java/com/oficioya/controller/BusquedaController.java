package com.oficioYa.controller;

import com.oficioYa.controller.docs.BusquedaApi;
import com.oficioYa.mapper.BusquedaMapper;
import com.oficioYa.mapper.PerfilTrabajadorMapper;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.model.dto.request.FiltroBusquedaDTO;
import com.oficioYa.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioYa.service.IBusquedaTrabajadorService;
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
