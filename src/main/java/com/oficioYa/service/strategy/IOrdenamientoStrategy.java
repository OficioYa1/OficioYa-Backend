package com.oficioYa.service.strategy;

import com.oficioYa.model.domain.CriteriosBusqueda;
import com.oficioYa.model.domain.PerfilTrabajador;

import java.util.List;

public interface IOrdenamientoStrategy {
    boolean aplica(String orden);
    List<PerfilTrabajador> ordenar(List<PerfilTrabajador> perfiles, CriteriosBusqueda criterios);
}
