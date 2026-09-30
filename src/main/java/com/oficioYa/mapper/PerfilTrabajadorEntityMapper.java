package com.oficioYa.mapper;

import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.persistence.entity.PerfilTrabajadorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class, OficioEntityMapper.class})
public interface PerfilTrabajadorEntityMapper {
    PerfilTrabajadorEntity toEntity(PerfilTrabajador domain);
    PerfilTrabajador toDomain(PerfilTrabajadorEntity entity);
}
