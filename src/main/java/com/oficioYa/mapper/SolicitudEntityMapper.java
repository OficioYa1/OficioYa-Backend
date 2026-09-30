package com.oficioYa.mapper;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.persistence.entity.SolicitudEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class})
public interface SolicitudEntityMapper {
    SolicitudEntity toEntity(Solicitud domain);
    Solicitud toDomain(SolicitudEntity entity);
}
