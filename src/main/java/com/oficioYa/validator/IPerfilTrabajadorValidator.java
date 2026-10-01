package com.oficioya.validator;

import com.oficioya.model.domain.FranjaDisponibilidad;

import java.util.List;

/** Reglas de negocio del perfil de trabajador (Dev B). */
public interface IPerfilTrabajadorValidator {

    /** El usuario debe existir (404), ser TRABAJADOR, estar activo y tener la cuenta verificada (422). */
    void validarCuentaElegible(Long usuarioId);

    /** El usuario no debe tener ya un perfil (409). */
    void validarPerfilNoExiste(Long usuarioId);

    /** Cada franja debe tener inicio < fin y no pueden solaparse franjas del mismo día (422). */
    void validarFranjasDisponibilidad(List<FranjaDisponibilidad> franjas);
}
