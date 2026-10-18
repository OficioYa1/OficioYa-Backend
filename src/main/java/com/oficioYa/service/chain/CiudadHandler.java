package com.oficioya.service.chain;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Eslabón 3: Búsqueda Ciudad Completa.
 * Si no hay resultados en el barrio ni en sectores aledaños, este eslabón
 * devuelve la totalidad de trabajadores activos en la ciudad para asegurar
 * que la solicitud del contratante tenga alternativas disponibles.
 */
@Component
@Order(3)
public class CiudadHandler extends AbstractBusquedaZonaHandler {

    @Override
    protected List<PerfilTrabajadorEntity> procesarNivel(String zona, PerfilTrabajadorRepository repository) {
        return repository.findAll();
    }

    @Override
    public String getNivel() {
        return "Eslabón 3 [Ciudad Completa]";
    }
}
