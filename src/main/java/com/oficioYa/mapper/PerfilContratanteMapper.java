package com.oficioYa.mapper;

import com.oficioYa.model.domain.PerfilContratante;
import com.oficioYa.model.dto.response.PerfilContratanteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfilContratanteMapper {
    @Mapping(source = "usuario.id", target = "usuarioId")
    PerfilContratanteResponseDTO toResponse(PerfilContratante domain);
}
