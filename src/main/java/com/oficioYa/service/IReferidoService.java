package com.oficioYa.service;

public interface IReferidoService {
    void registrarReferido(Long referenteId, String correoReferido);
    void procesarRegistroReferido(String correoNuevoUsuario);
}
