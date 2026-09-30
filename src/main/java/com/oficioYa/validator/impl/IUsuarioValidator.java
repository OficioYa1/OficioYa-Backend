package com.oficioYa.validator.impl;

public interface IUsuarioValidator {
   
    void validarExistencia(Long usuarioId);

    void validarCorreoUnico(String correo);
}
