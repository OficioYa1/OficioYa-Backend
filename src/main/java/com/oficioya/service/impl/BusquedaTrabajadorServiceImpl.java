package com.oficioya.service.impl;

import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.spec.PerfilTrabajadorSpecs;
import com.oficioya.service.IBusquedaTrabajadorService;
import com.oficioya.validator.IBusquedaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusquedaTrabajadorServiceImpl implements IBusquedaTrabajadorService {

    private final PerfilTrabajadorRepository repository;
    private final PerfilTrabajadorEntityMapper mapper;
    private final IBusquedaValidator busquedaValidator;

    private static final java.util.Map<String, Comparator<PerfilTrabajador>> SORTER_STRATEGIES = java.util.Map.of(
            "REPUTACION", Comparator.comparing(PerfilTrabajador::getCalificacionPromedio, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(PerfilTrabajador::getTrabajosCompletados, Comparator.nullsLast(Comparator.reverseOrder())),
            "DISTANCIA", Comparator.comparing(PerfilTrabajador::getZonaCobertura, Comparator.nullsLast(Comparator.naturalOrder()))
    );

    @Override
    public List<PerfilTrabajador> buscarTrabajadores(String zona, Long oficioId, Double calificacionMinima, String orden) {
        log.info("Iniciando busqueda de trabajadores. Zona: {}, Oficio: {}", zona, oficioId);
        
        List<PerfilTrabajador> candidatos = repository.findByZonaCoberturaContaining(zona).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        if (calificacionMinima != null) {
            candidatos = candidatos.stream()
                    .filter(p -> p.getCalificacionPromedio() != null && p.getCalificacionPromedio() >= calificacionMinima)
                    .collect(Collectors.toList());
        }

        String safeOrden = (orden != null) ? orden.toUpperCase() : "DISTANCIA";
        Comparator<PerfilTrabajador> comparator = SORTER_STRATEGIES.getOrDefault(safeOrden, SORTER_STRATEGIES.get("DISTANCIA"));

        return candidatos.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilTrabajador> buscar(CriteriosBusqueda criterios) {
        log.info("Búsqueda de trabajadores: categoria={}, oficioId={}, zona={}, tarifa=[{}, {}], dia={}, soloDisponiblesAhora={}",
                criterios.getCategoria(), criterios.getOficioId(), criterios.getZona(),
                criterios.getTarifaMin(), criterios.getTarifaMax(), criterios.getDia(), criterios.isSoloDisponiblesAhora());

        busquedaValidator.validarRangoTarifa(criterios);
        busquedaValidator.validarFranjaSolicitada(criterios);

        Specification<PerfilTrabajadorEntity> spec = PerfilTrabajadorSpecs.desdeCriterios(criterios);

        List<PerfilTrabajador> resultado = repository.findAll(spec, ordenSolicitado(criterios.getOrden())).stream()
                .map(mapper::toDomain)
                .toList();

        if (resultado.isEmpty()) {
            log.warn("Búsqueda sin resultados para los criterios dados");
        } else {
            log.info("Búsqueda completada: {} trabajadores", resultado.size());
        }
        return resultado;
    }

    /**
     * REPUTACION (por defecto): mejor calificación y más trabajos completados primero.
     * DISTANCIA: provisional por zona hasta tener coordenadas (RF-63, fuera del Sprint 02).
     */
    private Sort ordenSolicitado(String orden) {
        if (orden != null && orden.equalsIgnoreCase("DISTANCIA")) {
            return Sort.by(Sort.Order.asc("zonaCobertura"), Sort.Order.desc("calificacionPromedio"), Sort.Order.asc("id"));
        }
        return Sort.by(Sort.Order.desc("calificacionPromedio"), Sort.Order.desc("trabajosCompletados"), Sort.Order.asc("id"));
    }
}
