package com.oficioYa.exception;

/**
 * Lanzada cuando una operación entra en conflicto con el estado actual de los datos
 * (por ejemplo, un perfil que ya existe para el usuario).
 * Handler: 409 CONFLICT
 */
public class ConflictoException extends RuntimeException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
