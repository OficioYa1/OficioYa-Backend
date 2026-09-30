package com.oficioya.util;


public final class DistanciaGeograficaUtil {

    private DistanciaGeograficaUtil() {
    }

    public static double calcularDistancia(String zonaOrigen, String zonaDestino) {
        if (zonaOrigen == null || zonaDestino == null) {
            return Double.MAX_VALUE;
        }
        if (zonaOrigen.equalsIgnoreCase(zonaDestino)) {
            return 0.0;
        }
        // Simulación básica para Sprint 02
        return Math.random() * 20.0; // Devuelve una distancia aleatoria entre 0 y 20 km
    }
}
