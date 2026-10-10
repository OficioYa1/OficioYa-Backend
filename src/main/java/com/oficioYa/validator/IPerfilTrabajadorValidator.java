package com.oficioYa.validator;

import com.oficioYa.model.domain.FranjaDisponibilidad;
import com.oficioYa.model.domain.PerfilTrabajador;

import java.util.List;

/** Reglas de negocio del perfil de trabajador (Dev B). */
public interface IPerfilTrabajadorValidator {

    /** El usuario debe existir (404), ser TRABAJADOR, estar activo y tener la cuenta verificada (422). */
    void validarCuentaElegible(Long usuarioId);

    /** El usuario no debe tener ya un perfil (409). */
    void validarPerfilNoExiste(Long usuarioId);

    /** Cada franja debe tener inicio < fin y no pueden solaparse franjas del mismo día (422). */
    void validarFranjasDisponibilidad(List<FranjaDisponibilidad> franjas);

    /** RF-30: no puede estar ya activo (422) y debe tener zona de cobertura definida (422). */
    void validarPuedeActivarDisponibleAhora(PerfilTrabajador perfil);

    /** RF-31: debe estar activo para poder desactivarse (422). */
    void validarPuedeDesactivarDisponibleAhora(PerfilTrabajador perfil);
}
