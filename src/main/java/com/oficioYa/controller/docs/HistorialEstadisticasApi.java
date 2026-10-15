package com.oficioya.controller.docs;

import com.oficioya.model.dto.response.DashboardResumenDTO;
import com.oficioya.model.dto.response.EstadisticasResponseDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Historial y Estadisticas", description = "Operaciones de historial y estadisticas para clientes y trabajadores")
public interface HistorialEstadisticasApi {

    @Operation(summary = "Obtener historial de solicitudes de un cliente")
    @GetMapping("/cliente/{clienteId}/historial")
    ResponseEntity<List<SolicitudResponseDTO>> obtenerHistorialCliente(@PathVariable Long clienteId);

    @Operation(summary = "Obtener historial de trabajos de un trabajador")
    @GetMapping("/trabajador/{trabajadorId}/historial")
    ResponseEntity<List<SolicitudResponseDTO>> obtenerHistorialTrabajador(@PathVariable Long trabajadorId);

    @Operation(summary = "Obtener estadisticas de un cliente")
    @GetMapping("/cliente/{clienteId}/estadisticas")
    ResponseEntity<EstadisticasResponseDTO> obtenerEstadisticasCliente(@PathVariable Long clienteId);

    @Operation(summary = "Obtener estadisticas de un trabajador")
    @GetMapping("/trabajador/{trabajadorId}/estadisticas")
    ResponseEntity<EstadisticasResponseDTO> obtenerEstadisticasTrabajador(@PathVariable Long trabajadorId);

    @Operation(summary = "Obtener dashboard de resumen para un cliente")
    @GetMapping("/cliente/{clienteId}/dashboard")
    ResponseEntity<DashboardResumenDTO> obtenerDashboardCliente(@PathVariable Long clienteId);

    @Operation(summary = "Obtener dashboard de resumen para un trabajador")
    @GetMapping("/trabajador/{trabajadorId}/dashboard")
    ResponseEntity<DashboardResumenDTO> obtenerDashboardTrabajador(@PathVariable Long trabajadorId);
}
