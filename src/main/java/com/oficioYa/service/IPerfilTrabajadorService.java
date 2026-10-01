package com.oficioya.service;

import com.oficioya.model.domain.PerfilTrabajador;

import java.math.BigDecimal;

/** Casos de uso de configuración del perfil de trabajador. Trabaja solo con objetos de dominio. */
public interface IPerfilTrabajadorService {

    /** RF-01: crea la ficha pública asociada a una cuenta verificada. */
    PerfilTrabajador crearPerfil(PerfilTrabajador perfil);

    /** RF-04: define la zona de cobertura. */
    PerfilTrabajador actualizarZonaCobertura(Long perfilId, String zonaCobertura);

    /** RF-05: registra la tarifa aproximada por hora. */
    PerfilTrabajador actualizarTarifa(Long perfilId, BigDecimal tarifaPorHora);
}
