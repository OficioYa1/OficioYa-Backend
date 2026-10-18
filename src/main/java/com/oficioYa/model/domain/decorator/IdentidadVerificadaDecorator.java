package com.oficioya.model.domain.decorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Decorador concreto: Identidad Verificada.
 * Acredita que el trabajador ha validado su documento de identidad oficial.
 */
public class IdentidadVerificadaDecorator extends PerfilBaseDecorator {

    public IdentidadVerificadaDecorator(IPerfilVisualComponent wrappee) {
        super(wrappee);
    }

    @Override
    public List<String> getInsignias() {
        List<String> insignias = new ArrayList<>(super.getInsignias());
        insignias.add("Identidad Verificada (Cédula Auditada)");
        return insignias;
    }

    @Override
    public boolean esAltamenteConfiable() {
        return true;
    }

    @Override
    public String getBadgeDestacado() {
        return "Oficialmente Verificado";
    }
}
