package com.oficioya.mapper;

import com.oficioya.model.domain.Oficio;
import com.oficioya.persistence.entity.OficioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OficioEntityMapper {
    OficioEntity toEntity(Oficio domain);
    Oficio toDomain(OficioEntity entity);
}
