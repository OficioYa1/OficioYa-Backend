package com.oficioya.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardResumenDTO {
    private EstadisticasResponseDTO estadisticas;
    private List<SolicitudResponseDTO> historialReciente;
}
