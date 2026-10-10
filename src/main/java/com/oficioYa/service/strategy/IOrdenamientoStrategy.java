package com.oficioya.service.strategy;

import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.domain.PerfilTrabajador;

import java.util.List;

public interface IOrdenamientoStrategy {
    boolean aplica(String orden);
    List<PerfilTrabajador> ordenar(List<PerfilTrabajador> perfiles, CriteriosBusqueda criterios);
}
