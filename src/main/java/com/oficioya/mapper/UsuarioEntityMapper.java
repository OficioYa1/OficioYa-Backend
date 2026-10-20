package com.oficioya.mapper;

import com.oficioya.model.domain.Usuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {
    UsuarioEntity toEntity(Usuario domain);
    Usuario toDomain(UsuarioEntity entity);
}
