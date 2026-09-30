package com.oficioya.mapper;

import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerfilTrabajadorMapper {
    PerfilTrabajadorResponseDTO toResponse(PerfilTrabajador domain);
}
