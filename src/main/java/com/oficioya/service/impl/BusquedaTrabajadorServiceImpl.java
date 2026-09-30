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

    @Override
    public List<PerfilTrabajador> buscarTrabajadores(String zona, Long oficioId, Double calificacionMinima, String orden) {
        log.info("Iniciando busqueda de trabajadores. Zona: {}, Oficio: {}", zona, oficioId);
        
        List<PerfilTrabajador> candidatos = repository.findAll().stream()
                .map(mapper::toDomain)
                .filter(p -> p.getZonaCobertura() != null && p.getZonaCobertura().contains(zona))
                .collect(Collectors.toList());

        if (calificacionMinima != null) {
            candidatos = candidatos.stream()
                    .filter(p -> p.getCalificacionPromedio() != null && p.getCalificacionPromedio() >= calificacionMinima)
                    .collect(Collectors.toList());
        }

        Comparator<PerfilTrabajador> comparator;
        if ("REPUTACION".equalsIgnoreCase(orden)) {
            comparator = Comparator.comparing(PerfilTrabajador::getCalificacionPromedio, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(PerfilTrabajador::getTrabajosCompletados, Comparator.nullsLast(Comparator.reverseOrder()));
        } else {
            comparator = Comparator.comparing(PerfilTrabajador::getZonaCobertura, Comparator.nullsLast(Comparator.naturalOrder()));
        }

        return candidatos.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
