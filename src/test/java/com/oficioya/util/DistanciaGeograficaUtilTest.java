package com.oficioya.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanciaGeograficaUtilTest {

    @Test
    @DisplayName("calcularDistancia - origen o destino nulo retorna Double.MAX_VALUE")
    void calcularDistancia_nulos_retornaMaxValue() {
        assertEquals(Double.MAX_VALUE, DistanciaGeograficaUtil.calcularDistancia(null, "Norte"));
        assertEquals(Double.MAX_VALUE, DistanciaGeograficaUtil.calcularDistancia("Sur", null));
        assertEquals(Double.MAX_VALUE, DistanciaGeograficaUtil.calcularDistancia(null, null));
    }

    @Test
    @DisplayName("calcularDistancia - misma zona retorna 0.0")
    void calcularDistancia_mismaZona_retornaCero() {
        assertEquals(0.0, DistanciaGeograficaUtil.calcularDistancia("Norte", "norte"));
        assertEquals(0.0, DistanciaGeograficaUtil.calcularDistancia("Chapinero", "CHAPINERO"));
    }

    @Test
    @DisplayName("calcularDistancia - zonas distintas retorna distancia entre 0 y 20 km")
    void calcularDistancia_zonasDistintas_retornaValorValido() {
        double distancia = DistanciaGeograficaUtil.calcularDistancia("Norte", "Sur");
        assertTrue(distancia >= 0.0 && distancia <= 20.0);
    }
}
