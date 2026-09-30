package com.oficioya.service;

import com.oficioya.model.domain.PerfilTrabajador;
import java.util.List;

public interface IBusquedaTrabajadorService {
    List<PerfilTrabajador> buscarTrabajadores(String zona, Long oficioId, Double calificacionMinima, String orden);
}
