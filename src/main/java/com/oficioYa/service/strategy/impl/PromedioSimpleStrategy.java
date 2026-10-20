package com.oficioya.service.strategy.impl;

import com.oficioya.persistence.entity.ResenaEntity;
import com.oficioya.service.strategy.ICalculoReputacionStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Estrategia de cálculo de reputación por media aritmética simple tradicional.
 */
@Primary
@Component
public class PromedioSimpleStrategy implements ICalculoReputacionStrategy {

    @Override
    public Double calcularPromedio(List<ResenaEntity> resenas) {
        if (resenas == null || resenas.isEmpty()) {
            return 0.0;
        }

        List<ResenaEntity> validas = resenas.stream()
                .filter(r -> r != null && !r.isReportada() && r.getEstrellas() != null)
                .toList();

        if (validas.isEmpty()) {
            return 0.0;
        }

        double suma = validas.stream().mapToInt(ResenaEntity::getEstrellas).sum();
        double promedio = suma / validas.size();
        return Math.round(promedio * 10.0) / 10.0;
    }

    @Override
    public String getNombre() {
        return "Promedio Simple Tradicional";
    }
}
