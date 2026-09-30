package com.oficioya.mapper;

import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class, OficioEntityMapper.class})
public interface PerfilTrabajadorEntityMapper {
    PerfilTrabajadorEntity toEntity(PerfilTrabajador domain);
    PerfilTrabajador toDomain(PerfilTrabajadorEntity entity);
}
