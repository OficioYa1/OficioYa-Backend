package com.oficioya.mapper;

import com.oficioya.model.dto.response.OficioResponseDTO;
import com.oficioya.persistence.entity.OficioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OficioMapper {
    OficioResponseDTO toDto(OficioEntity entity);
    List<OficioResponseDTO> toDtoList(List<OficioEntity> entityList);
}
