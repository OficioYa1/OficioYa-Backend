package com.oficioYa.validator;

public interface IUsuarioValidator {
   
    void validarExistencia(Long usuarioId);

    void validarCorreoUnico(String correo);
}
