package com.oficioya.service;

public interface IReferidoService {
    void registrarReferido(String correoReferente, String correoReferido);
    void procesarRegistroReferido(String correoNuevoUsuario);
}
