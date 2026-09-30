package com.oficioYa.mapper;

import com.oficioYa.model.domain.Oficio;
import com.oficioYa.persistence.entity.OficioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OficioEntityMapper {
    OficioEntity toEntity(Oficio domain);
    Oficio toDomain(OficioEntity entity);
}
