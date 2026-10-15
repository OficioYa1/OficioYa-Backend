package com.oficioya.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@Tag(name = "Health Check", description = "Endpoint público para monitoreo y estado del servicio")
public class HealthController {

    @GetMapping("/api/v1/health")
    @Operation(summary = "Verifica el estado del servicio")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "OficioYa-Backend",
                "timestamp", Instant.now().toString()
        ));
    }

    @GetMapping("/")
    @Operation(summary = "Redirección a la documentación Swagger UI")
    public ResponseEntity<Void> root() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/swagger-ui/index.html")
                .build();
    }
}
