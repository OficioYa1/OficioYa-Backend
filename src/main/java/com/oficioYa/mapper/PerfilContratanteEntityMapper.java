package com.oficioya.mapper;

import com.oficioya.model.domain.PerfilContratante;
import com.oficioya.persistence.entity.PerfilContratanteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class})
public interface PerfilContratanteEntityMapper {
    PerfilContratante toDomain(PerfilContratanteEntity entity);
    PerfilContratanteEntity toEntity(PerfilContratante domain);
}
