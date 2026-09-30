package com.oficioya.service.impl;

import com.oficioya.mapper.PerfilTrabajadorEntityMapper;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.service.IBusquedaTrabajadorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusquedaTrabajadorServiceImpl implements IBusquedaTrabajadorService {

    private final PerfilTrabajadorRepository repository;
    private final PerfilTrabajadorEntityMapper mapper;

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
}
