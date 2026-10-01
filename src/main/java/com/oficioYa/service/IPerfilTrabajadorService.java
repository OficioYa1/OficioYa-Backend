package com.oficioya.service;

import com.oficioya.model.domain.PerfilTrabajador;

/** Casos de uso de configuración del perfil de trabajador. Trabaja solo con objetos de dominio. */
public interface IPerfilTrabajadorService {

    /** RF-01: crea la ficha pública asociada a una cuenta verificada. */
    PerfilTrabajador crearPerfil(PerfilTrabajador perfil);
}
