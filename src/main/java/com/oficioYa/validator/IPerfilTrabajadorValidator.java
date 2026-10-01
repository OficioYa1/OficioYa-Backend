package com.oficioya.validator;

/** Reglas de negocio del perfil de trabajador (Dev B). */
public interface IPerfilTrabajadorValidator {

    /** El usuario debe existir (404), ser TRABAJADOR, estar activo y tener la cuenta verificada (422). */
    void validarCuentaElegible(Long usuarioId);

    /** El usuario no debe tener ya un perfil (409). */
    void validarPerfilNoExiste(Long usuarioId);

    /** El perfil debe existir (404). */
    void validarPerfilExiste(Long perfilId);
}
