package com.oficioya.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    @DisplayName("GET /api/v1/health debe retornar 200 OK con status UP")
    void testHealthCheck() {
        ResponseEntity<Map<String, Object>> response = healthController.healthCheck();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("OficioYa-Backend", response.getBody().get("service"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    @DisplayName("GET / debe redirigir a Swagger UI")
    void testRootRedirect() {
        ResponseEntity<Void> response = healthController.root();
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("/swagger-ui/index.html", response.getHeaders().getFirst("Location"));
    }
}
