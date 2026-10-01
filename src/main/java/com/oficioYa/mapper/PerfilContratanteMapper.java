package com.oficioya.mapper;

import com.oficioya.model.domain.PerfilContratante;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfilContratanteMapper {
    @Mapping(source = "usuario.id", target = "usuarioId")
    PerfilContratanteResponseDTO toResponse(PerfilContratante domain);
}
