package com.oficioYa.validator.impl;

import com.oficioYa.model.domain.CriteriosBusqueda;
import com.oficioYa.model.exception.ReglaDeNegocioException;
import com.oficioYa.validator.IBusquedaValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusquedaValidatorImpl implements IBusquedaValidator {

    @Override
    public void validarRangoTarifa(CriteriosBusqueda criterios) {
        if (criterios.getTarifaMin() != null && criterios.getTarifaMax() != null
                && criterios.getTarifaMin().compareTo(criterios.getTarifaMax()) > 0) {
            log.warn("Búsqueda con rango de tarifa inválido: min={}, max={}", criterios.getTarifaMin(), criterios.getTarifaMax());
            throw new ReglaDeNegocioException("La tarifa mínima no puede ser mayor que la tarifa máxima");
        }
    }

    @Override
    public void validarFranjaSolicitada(CriteriosBusqueda criterios) {
        boolean hayHoras = criterios.getHoraDesde() != null || criterios.getHoraHasta() != null;
        if (hayHoras && criterios.getDia() == null) {
            log.warn("Búsqueda con horas pero sin día");
            throw new ReglaDeNegocioException("Para filtrar por hora debes indicar también el día");
        }
        if (criterios.getHoraHasta() != null && criterios.getHoraDesde() == null) {
            log.warn("Búsqueda con horaHasta pero sin horaDesde");
            throw new ReglaDeNegocioException("Para indicar horaHasta debes indicar también horaDesde");
        }
        if (criterios.getHoraDesde() != null && criterios.getHoraHasta() != null
                && !criterios.getHoraDesde().isBefore(criterios.getHoraHasta())) {
            log.warn("Búsqueda con franja inválida: desde={}, hasta={}", criterios.getHoraDesde(), criterios.getHoraHasta());
            throw new ReglaDeNegocioException("La hora de inicio debe ser anterior a la hora de fin");
        }
    }
}
