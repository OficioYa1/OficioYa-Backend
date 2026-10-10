package com.oficioYa.exception;

/**
 * Lanzada cuando se intenta crear un usuario con un correo que ya existe.
 * Handler: 409 CONFLICT
 */
public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String mensaje) {
        super(mensaje);
    }
}
