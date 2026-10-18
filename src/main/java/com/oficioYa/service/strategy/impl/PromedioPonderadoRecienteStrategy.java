package com.oficioya.service.strategy.impl;

import com.oficioya.persistence.entity.ResenaEntity;
import com.oficioya.service.strategy.ICalculoReputacionStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estrategia de cálculo ponderada por antigüedad:
 * Reseñas de los últimos 30 días tienen un peso de 1.5x,
 * mientras que reseñas más antiguas tienen un peso de 1.0x.
 */
@Component
public class PromedioPonderadoRecienteStrategy implements ICalculoReputacionStrategy {

    private static final int DIAS_RECIENTES = 30;
    private static final double PESO_RECIENTE = 1.5;
    private static final double PESO_ANTIGUO = 1.0;

    @Override
    public Double calcularPromedio(List<ResenaEntity> resenas) {
        if (resenas == null || resenas.isEmpty()) {
            return 0.0;
        }

        LocalDateTime limiteReciente = LocalDateTime.now().minusDays(DIAS_RECIENTES);

        double sumaPonderada = 0.0;
        double sumaPesos = 0.0;

        for (ResenaEntity r : resenas) {
            if (r == null || r.isReportada() || r.getEstrellas() == null) {
                continue;
            }

            boolean esReciente = r.getFechaCreacion() != null && r.getFechaCreacion().isAfter(limiteReciente);
            double peso = esReciente ? PESO_RECIENTE : PESO_ANTIGUO;

            sumaPonderada += r.getEstrellas() * peso;
            sumaPesos += peso;
        }

        if (sumaPesos == 0.0) {
            return 0.0;
        }

        double promedio = sumaPonderada / sumaPesos;
        return Math.round(promedio * 10.0) / 10.0;
    }

    @Override
    public String getNombre() {
        return "Promedio Ponderado por Antigüedad";
    }
}
