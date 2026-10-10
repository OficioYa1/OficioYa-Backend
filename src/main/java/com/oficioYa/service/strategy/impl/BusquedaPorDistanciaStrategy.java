package com.oficioYa.service.strategy.impl;

import com.oficioYa.adapter.IMapAdapter;
import com.oficioYa.model.domain.CriteriosBusqueda;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.service.strategy.IOrdenamientoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusquedaPorDistanciaStrategy implements IOrdenamientoStrategy {

    private final IMapAdapter mapAdapter;

    @Override
    public boolean aplica(String orden) {
        return orden != null && orden.equalsIgnoreCase("DISTANCIA");
    }

    @Override
    public List<PerfilTrabajador> ordenar(List<PerfilTrabajador> perfiles, CriteriosBusqueda criterios) {
        final String zonaOrigen = criterios != null ? criterios.getZona() : null;

        return perfiles.stream()
                .sorted((p1, p2) -> {
                    double dist1 = mapAdapter.calcularDistancia(zonaOrigen, p1.getZonaCobertura());
                    double dist2 = mapAdapter.calcularDistancia(zonaOrigen, p2.getZonaCobertura());
                    int cmp = Double.compare(dist1, dist2);
                    if (cmp != 0) {
                        return cmp;
                    }
                    // Si están a la misma distancia, desempatamos por reputación
                    Comparator<PerfilTrabajador> desempate = Comparator.comparing(PerfilTrabajador::getCalificacionPromedio, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(PerfilTrabajador::getTrabajosCompletados, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(PerfilTrabajador::getId, Comparator.nullsLast(Comparator.naturalOrder()));
                    return desempate.compare(p1, p2);
                })
                .collect(Collectors.toList());
    }
}
