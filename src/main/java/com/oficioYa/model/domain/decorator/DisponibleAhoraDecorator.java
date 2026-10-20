package com.oficioya.model.domain.decorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Decorador concreto: Disponible Ahora.
 * Resalta la disponibilidad operativa inmediata del trabajador para emergencias.
 */
public class DisponibleAhoraDecorator extends PerfilBaseDecorator {

    public DisponibleAhoraDecorator(IPerfilVisualComponent wrappee) {
        super(wrappee);
    }

    @Override
    public List<String> getInsignias() {
        List<String> insignias = new ArrayList<>(super.getInsignias());
        insignias.add("Disponible Ahora (Atención Inmediata)");
        return insignias;
    }

    @Override
    public String getBadgeDestacado() {
        return "⚡ Disponible Inmediato";
    }
}
