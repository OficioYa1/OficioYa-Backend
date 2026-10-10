package com.oficioYa.mapper;

import com.oficioYa.model.domain.PerfilContratante;
import com.oficioYa.persistence.entity.PerfilContratanteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class})
public interface PerfilContratanteEntityMapper {
    PerfilContratante toDomain(PerfilContratanteEntity entity);
    PerfilContratanteEntity toEntity(PerfilContratante domain);
}
