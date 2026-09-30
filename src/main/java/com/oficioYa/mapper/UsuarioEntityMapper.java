package com.oficioYa.mapper;

import com.oficioYa.model.domain.Usuario;
import com.oficioYa.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {
    UsuarioEntity toEntity(Usuario domain);
    Usuario toDomain(UsuarioEntity entity);
}
