package com.oficioya.mapper;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.dto.request.SolicitudCreacionDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoEnum", ignore = true)
    @Mapping(target = "estadoActual", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "trabajador", ignore = true)
    Solicitud toDomain(SolicitudCreacionDTO dto);

    @Mapping(target = "contratanteId", source = "contratante.id")
    @Mapping(target = "trabajadorId", source = "trabajador.id")
    @Mapping(target = "estado", source = "estadoEnum")
    SolicitudResponseDTO toResponse(Solicitud domain);
}
