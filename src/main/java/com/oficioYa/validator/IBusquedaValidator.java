package com.oficioya.validator;

import com.oficioya.model.domain.CriteriosBusqueda;

/** Reglas de negocio de la búsqueda de trabajadores (Dev B). */
public interface IBusquedaValidator {

    /** RF-14: tarifaMin no puede superar a tarifaMax (422). */
    void validarRangoTarifa(CriteriosBusqueda criterios);

    /** RF-15: las horas exigen un día y deben formar un rango válido (422). */
    void validarFranjaSolicitada(CriteriosBusqueda criterios);
}
