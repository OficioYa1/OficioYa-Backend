package com.oficioya.service.chain;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Patrón Chain of Responsibility (Eslabón Base).
 * Define el contrato y la referencia al siguiente eslabón en la cadena
 * de resolución geográfica para la búsqueda inteligente de trabajadores.
 */
@Slf4j
public abstract class AbstractBusquedaZonaHandler {

    @Getter
    protected AbstractBusquedaZonaHandler siguiente;

    public AbstractBusquedaZonaHandler setSiguiente(AbstractBusquedaZonaHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    /**
     * Intenta resolver la búsqueda en el nivel geográfico actual.
     * Si no encuentra resultados, delega la búsqueda al siguiente eslabón.
     */
    public List<PerfilTrabajadorEntity> buscar(String zona, PerfilTrabajadorRepository repository) {
        if (zona == null || zona.isBlank()) {
            return delegar(zona, repository);
        }

        List<PerfilTrabajadorEntity> resultados = procesarNivel(zona.trim(), repository);
        if (resultados != null && !resultados.isEmpty()) {
            log.info("[Chain-Of-Responsibility] {} resolvió la búsqueda para zona '{}' con {} resultados.",
                    getNivel(), zona, resultados.size());
            return resultados;
        }

        log.debug("[Chain-Of-Responsibility] {} no encontró resultados para '{}'. Delegando al siguiente eslabón...",
                getNivel(), zona);
        return delegar(zona, repository);
    }

    protected List<PerfilTrabajadorEntity> delegar(String zona, PerfilTrabajadorRepository repository) {
        if (siguiente != null) {
            return siguiente.buscar(zona, repository);
        }
        return Collections.emptyList();
    }

    protected abstract List<PerfilTrabajadorEntity> procesarNivel(String zona, PerfilTrabajadorRepository repository);

    public abstract String getNivel();
}
