package com.oficioya.service.chain;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Eslabón 1: Búsqueda Barrio Exacto.
 * Intenta encontrar trabajadores cuya zona de cobertura coincida exactamente
 * (sin distinción de mayúsculas o minúsculas) con la zona requerida.
 */
@Component
@Order(1)
public class BarrioExactoHandler extends AbstractBusquedaZonaHandler {

    @Override
    protected List<PerfilTrabajadorEntity> procesarNivel(String zona, PerfilTrabajadorRepository repository) {
        return repository.findAll().stream()
                .filter(p -> p.getZonaCobertura() != null && p.getZonaCobertura().trim().equalsIgnoreCase(zona))
                .collect(Collectors.toList());
    }

    @Override
    public String getNivel() {
        return "Eslabón 1 [Barrio Exacto]";
    }
}
