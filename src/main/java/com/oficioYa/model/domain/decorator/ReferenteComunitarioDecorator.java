package com.oficioya.model.domain.decorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Decorador concreto: Referente Comunitario Verificado.
 * Concede el sello de confianza vecinal al trabajador que ha recomendado
 * exitosamente colegas en tres o más oficios distintos.
 */
public class ReferenteComunitarioDecorator extends PerfilBaseDecorator {

    public ReferenteComunitarioDecorator(IPerfilVisualComponent wrappee) {
        super(wrappee);
    }

    @Override
    public List<String> getInsignias() {
        List<String> insignias = new ArrayList<>(super.getInsignias());
        insignias.add("Referente Comunitario Verificado (Conector Vecinal)");
        return insignias;
    }

    @Override
    public boolean esAltamenteConfiable() {
        return true;
    }

    @Override
    public String getBadgeDestacado() {
        return "Líder Comunitario";
    }
}
