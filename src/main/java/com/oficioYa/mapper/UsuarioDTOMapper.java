package com.oficioYa.mapper;

import com.oficioYa.model.domain.Usuario;
import com.oficioYa.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioYa.model.dto.response.UsuarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "correoVerificado", constant = "false")
    @Mapping(target = "telefonoVerificado", constant = "false")
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fotoPerfil", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    Usuario toDomain(UsuarioRegistroRequestDTO requestDTO);

    UsuarioResponseDTO toResponseDTO(Usuario domain);
}
