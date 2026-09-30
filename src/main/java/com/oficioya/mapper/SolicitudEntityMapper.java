package com.oficioya.mapper;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.persistence.entity.SolicitudEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class})
public interface SolicitudEntityMapper {
    SolicitudEntity toEntity(Solicitud domain);
    Solicitud toDomain(SolicitudEntity entity);
}
