package com.oficioYa.service;

import com.oficioYa.model.domain.CriteriosBusqueda;
import com.oficioYa.model.domain.PerfilTrabajador;
import java.util.List;

public interface IBusquedaTrabajadorService {
    List<PerfilTrabajador> buscarTrabajadores(String zona, Long oficioId, Double calificacionMinima, String orden);

    /**
     * RF-11 a RF-15 y RF-32 (más calificación y orden de RF-16/17/18): búsqueda con filtros
     * opcionales y combinables resuelta en base de datos.
     */
    List<PerfilTrabajador> buscar(CriteriosBusqueda criterios);
}
