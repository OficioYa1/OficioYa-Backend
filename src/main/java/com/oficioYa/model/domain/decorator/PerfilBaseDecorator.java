package com.oficioya.model.domain.decorator;

import java.util.List;

/**
 * Decorador abstracto base que implementa IPerfilVisualComponent
 * y mantiene la referencia al componente envuelto (wrappee).
 */
public abstract class PerfilBaseDecorator implements IPerfilVisualComponent {

    protected final IPerfilVisualComponent wrappee;

    protected PerfilBaseDecorator(IPerfilVisualComponent wrappee) {
        if (wrappee == null) {
            throw new IllegalArgumentException("El componente a decorar no puede ser nulo");
        }
        this.wrappee = wrappee;
    }

    @Override
    public String getNombreVisible() {
        return wrappee.getNombreVisible();
    }

    @Override
    public List<String> getInsignias() {
        return wrappee.getInsignias();
    }

    @Override
    public boolean esAltamenteConfiable() {
        return wrappee.esAltamenteConfiable();
    }

    @Override
    public String getBadgeDestacado() {
        return wrappee.getBadgeDestacado();
    }
}
