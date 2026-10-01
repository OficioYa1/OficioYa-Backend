package com.oficioya.mapper;

import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.FranjaDisponibilidadRequestDTO;
import com.oficioya.model.dto.request.PerfilTrabajadorCreacionRequestDTO;
import com.oficioya.model.dto.response.FranjaDisponibilidadResponseDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/** Mapper IN: traduce entre la capa de presentación (DTOs) y el dominio. */
@Mapper(componentModel = "spring")
public interface PerfilTrabajadorMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "usuarioId", target = "usuario.id")
    @Mapping(source = "descripcion", target = "descripcion")
    PerfilTrabajador toDomain(PerfilTrabajadorCreacionRequestDTO request);

    @Mapping(source = "usuario.id", target = "usuarioId")
    PerfilTrabajadorResponseDTO toResponse(PerfilTrabajador domain);

    FranjaDisponibilidad toFranjaDomain(FranjaDisponibilidadRequestDTO request);

    List<FranjaDisponibilidad> toFranjasDomain(List<FranjaDisponibilidadRequestDTO> requests);

    FranjaDisponibilidadResponseDTO toFranjaResponse(FranjaDisponibilidad domain);
}
