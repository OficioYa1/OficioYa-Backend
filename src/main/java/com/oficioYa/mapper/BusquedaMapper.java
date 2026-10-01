package com.oficioya.mapper;

import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.dto.request.FiltroBusquedaDTO;
import org.mapstruct.Mapper;

/** Mapper IN de la búsqueda: filtros HTTP -> criterios de dominio. */
@Mapper(componentModel = "spring")
public interface BusquedaMapper {
    CriteriosBusqueda toCriterios(FiltroBusquedaDTO filtro);
}
