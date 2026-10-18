package com.oficioya.model.domain.decorator;

/**
 * Patrón Combinado Builder + Decorator.
 * Ensambla paso a paso el objeto IPerfilVisualComponent envolviéndolo con
 * los decoradores de mérito correspondientes según el estado del trabajador.
 */
public class PerfilVisualBuilder {

    private IPerfilVisualComponent component;

    public PerfilVisualBuilder(String nombre) {
        this.component = new PerfilBaseComponent(nombre);
    }

    public static PerfilVisualBuilder paraTrabajador(String nombre) {
        return new PerfilVisualBuilder(nombre);
    }

    public PerfilVisualBuilder conIdentidadVerificada(boolean verificada) {
        if (verificada) {
            this.component = new IdentidadVerificadaDecorator(this.component);
        }
        return this;
    }

    public PerfilVisualBuilder conReferenteComunitario(boolean esReferente) {
        if (esReferente) {
            this.component = new ReferenteComunitarioDecorator(this.component);
        }
        return this;
    }

    public PerfilVisualBuilder conDisponibleAhora(boolean disponible) {
        if (disponible) {
            this.component = new DisponibleAhoraDecorator(this.component);
        }
        return this;
    }

    public IPerfilVisualComponent build() {
        return this.component;
    }
}
