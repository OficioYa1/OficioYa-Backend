package com.oficioya.mapper;

import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfilTrabajadorMapper {
    @Mapping(source = "usuario.id", target = "usuarioId")
    PerfilTrabajadorResponseDTO toResponse(PerfilTrabajador domain);
}
