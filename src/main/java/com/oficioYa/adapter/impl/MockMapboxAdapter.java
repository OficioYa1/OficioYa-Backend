package com.oficioya.adapter.impl;

import com.oficioya.adapter.IMapAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockMapboxAdapter implements IMapAdapter {

    @Override
    public double calcularDistancia(String zonaOrigen, String zonaDestino) {
        if (zonaOrigen == null || zonaDestino == null) {
            return Double.MAX_VALUE;
        }
        if (zonaOrigen.equalsIgnoreCase(zonaDestino)) {
            return 0.0;
        }
        
        log.info("Mock Mapbox: Calculando distancia entre {} y {}...", zonaOrigen, zonaDestino);
        
        // Simulación básica: Devuelve una distancia determinista basada en el hash de los strings
        // Esto permite que el ordenamiento siempre sea consistente
        int hashDistancia = Math.abs(zonaOrigen.hashCode() ^ zonaDestino.hashCode());
        double distancia = (hashDistancia % 200) / 10.0; // Distancia entre 0.0 y 20.0 km
        
        log.info("Mock Mapbox: Distancia calculada: {} km", distancia);
        return distancia;
    }
}
