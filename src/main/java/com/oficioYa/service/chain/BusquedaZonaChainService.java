package com.oficioya.service.chain;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Servicio orquestador del patrón Chain of Responsibility.
 * Ensambla los eslabones: BarrioExacto -> BarriosCercanos -> CiudadCompleta
 * y expone la ejecución de la cadena.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusquedaZonaChainService {

    private final BarrioExactoHandler barrioExactoHandler;
    private final BarriosCercanosHandler barriosCercanosHandler;
    private final CiudadHandler ciudadHandler;

    private AbstractBusquedaZonaHandler cabezaCadena;

    @PostConstruct
    public void ensamblarCadena() {
        barrioExactoHandler.setSiguiente(barriosCercanosHandler);
        barriosCercanosHandler.setSiguiente(ciudadHandler);
        this.cabezaCadena = barrioExactoHandler;
        log.info("[Chain-Of-Responsibility] Cadena de búsqueda geográfica ensamblada exitosamente: " +
                "BarrioExacto -> BarriosCercanos -> CiudadCompleta");
    }

    public List<PerfilTrabajadorEntity> resolverBusquedaPorZona(String zona, PerfilTrabajadorRepository repository) {
        if (cabezaCadena == null) {
            ensamblarCadena();
        }
        if (zona == null || zona.isBlank()) {
            return Collections.emptyList();
        }
        return cabezaCadena.buscar(zona, repository);
    }
}
