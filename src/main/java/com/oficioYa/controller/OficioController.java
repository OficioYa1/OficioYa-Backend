package com.oficioya.controller;

import com.oficioya.controller.docs.OficioApi;
import com.oficioya.model.dto.response.OficioResponseDTO;
import com.oficioya.service.IOficioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/oficios")
@RequiredArgsConstructor
public class OficioController implements OficioApi {

    private final IOficioService oficioService;

    @Override
    @GetMapping
    public ResponseEntity<List<OficioResponseDTO>> listarOficiosActivos() {
        return ResponseEntity.ok(oficioService.listarOficiosActivos());
    }
}
