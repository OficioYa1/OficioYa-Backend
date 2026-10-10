package com.oficioYa.mapper;

import com.oficioYa.model.domain.Solicitud;
import com.oficioYa.persistence.entity.SolicitudEntity;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class})
public interface SolicitudEntityMapper {
    @Mapping(target = "estado", source = "estadoEnum")
    SolicitudEntity toEntity(Solicitud domain);
    
    @Mapping(target = "estadoEnum", source = "estado")
    @Mapping(target = "estadoActual", ignore = true)
    Solicitud toDomain(SolicitudEntity entity);
}
