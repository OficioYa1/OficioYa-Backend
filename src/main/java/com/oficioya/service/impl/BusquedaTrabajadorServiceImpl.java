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
import com.oficioya.service.strategy.IOrdenamientoStrategy;
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
    private final List<IOrdenamientoStrategy> estrategiasOrdenamiento;

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

        IOrdenamientoStrategy estrategia = estrategiasOrdenamiento.stream()
                .filter(e -> e.aplica(orden))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay estrategia para el orden: " + orden));

        CriteriosBusqueda temp = new CriteriosBusqueda();
        temp.setZona(zona);

        return estrategia.ordenar(candidatos, temp);
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

        List<PerfilTrabajador> perfiles = repository.findAll(spec).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        IOrdenamientoStrategy estrategia = estrategiasOrdenamiento.stream()
                .filter(e -> e.aplica(criterios.getOrden()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay estrategia para el orden: " + criterios.getOrden()));

        List<PerfilTrabajador> resultado = estrategia.ordenar(perfiles, criterios);

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


}
