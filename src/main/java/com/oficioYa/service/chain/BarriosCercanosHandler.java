package com.oficioya.service.chain;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Eslabón 2: Búsqueda Barrios Cercanos / Radio Ampliado.
 * Si el barrio exacto no arrojó resultados, este eslabón busca trabajadores
 * cuya zona de cobertura contenga el término o pertenezca al mismo sector/localidad.
 */
@Component
@Order(2)
public class BarriosCercanosHandler extends AbstractBusquedaZonaHandler {

    @Override
    protected List<PerfilTrabajadorEntity> procesarNivel(String zona, PerfilTrabajadorRepository repository) {
        String zonaLower = zona.toLowerCase();
        return repository.findAll().stream()
                .filter(p -> p.getZonaCobertura() != null &&
                        (p.getZonaCobertura().toLowerCase().contains(zonaLower) ||
                         zonaLower.contains(p.getZonaCobertura().toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public String getNivel() {
        return "Eslabón 2 [Barrios Cercanos]";
    }
}
