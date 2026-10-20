package com.oficioya.model.domain.decorator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Componente concreto base que representa el perfil estándar de un trabajador
 * antes de recibir decoradores de mérito o certificaciones.
 */
public class PerfilBaseComponent implements IPerfilVisualComponent {

    private final String nombre;

    public PerfilBaseComponent(String nombre) {
        this.nombre = nombre != null ? nombre : "Trabajador OficioYa";
    }

    @Override
    public String getNombreVisible() {
        return nombre;
    }

    @Override
    public List<String> getInsignias() {
        return new ArrayList<>(Collections.singletonList("Perfil Básico"));
    }

    @Override
    public boolean esAltamenteConfiable() {
        return false;
    }

    @Override
    public String getBadgeDestacado() {
        return "Estándar";
    }
}
