package com.oficioYa.validator;

public interface ISolicitudValidator {

    /**
     * Valida que el usuario exista, este activo y tenga rol CONTRATANTE.
     */
    void validarContratante(Long contratanteId);
}
