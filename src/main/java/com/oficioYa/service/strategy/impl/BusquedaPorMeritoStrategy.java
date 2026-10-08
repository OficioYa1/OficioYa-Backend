package com.oficioya.service.strategy.impl;

import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.service.strategy.IOrdenamientoStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusquedaPorMeritoStrategy implements IOrdenamientoStrategy {

    @Override
    public boolean aplica(String orden) {
        return orden == null || orden.trim().isEmpty() || orden.equalsIgnoreCase("REPUTACION");
    }

    @Override
    public List<PerfilTrabajador> ordenar(List<PerfilTrabajador> perfiles, CriteriosBusqueda criterios) {
        return perfiles.stream()
                .sorted(Comparator.comparing(PerfilTrabajador::getCalificacionPromedio, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(PerfilTrabajador::getTrabajosCompletados, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(PerfilTrabajador::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}
