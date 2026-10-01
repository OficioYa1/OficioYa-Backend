package com.oficioya.service.impl;

import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.repository.OficioRepository;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.spec.PerfilTrabajadorSpecs;
import com.oficioya.service.IBusquedaTrabajadorService;
import com.oficioya.util.InterpreteNecesidadUtil;
import com.oficioya.validator.IBusquedaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusquedaTrabajadorServiceImpl implements IBusquedaTrabajadorService {

    private final PerfilTrabajadorRepository repository;
    private final PerfilTrabajadorEntityMapper mapper;
    private final OficioRepository oficioRepository;
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
        log.info("Búsqueda de trabajadores: texto={}, categoria={}, oficioId={}, zona={}, tarifa=[{}, {}], dia={}, soloDisponiblesAhora={}",
                criterios.getTexto(), criterios.getCategoria(), criterios.getOficioId(), criterios.getZona(),
                criterios.getTarifaMin(), criterios.getTarifaMax(), criterios.getDia(), criterios.isSoloDisponiblesAhora());

        busquedaValidator.validarRangoTarifa(criterios);
        busquedaValidator.validarFranjaSolicitada(criterios);

        List<String> terminos = InterpreteNecesidadUtil.extraerTerminos(criterios.getTexto());
        if (criterios.getTexto() != null && !criterios.getTexto().isBlank() && terminos.isEmpty()) {
            log.warn("El texto de búsqueda no aporta términos útiles: '{}'", criterios.getTexto());
        }
        Set<Long> oficiosDelTexto = resolverOficios(terminos);

        Specification<PerfilTrabajadorEntity> spec =
                PerfilTrabajadorSpecs.desdeCriterios(criterios, oficiosDelTexto, terminos);

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

    /** RF-11: oficios activos del catálogo cuyo nombre, categoría o descripción contienen algún término. */
    private Set<Long> resolverOficios(List<String> terminos) {
        if (terminos.isEmpty()) {
            return Set.of();
        }
        return oficioRepository.findAll().stream()
                .filter(OficioEntity::isActivo)
                .filter(o -> coincide(o, terminos))
                .map(OficioEntity::getId)
                .collect(Collectors.toSet());
    }

    private boolean coincide(OficioEntity oficio, List<String> terminos) {
        String texto = InterpreteNecesidadUtil.normalizar(
                oficio.getNombre() + " " + oficio.getCategoria() + " " + (oficio.getDescripcion() == null ? "" : oficio.getDescripcion()));
        return terminos.stream().anyMatch(texto::contains);
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
