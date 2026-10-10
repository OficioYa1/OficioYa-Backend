package com.oficioya.mapper;

import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.persistence.entity.FranjaDisponibilidadEmbeddable;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class, OficioEntityMapper.class})
public interface PerfilTrabajadorEntityMapper {
    @Mapping(target = "fotosPortafolio", ignore = true)
    PerfilTrabajador toDomain(PerfilTrabajadorEntity entity);
    
    PerfilTrabajadorEntity toEntity(PerfilTrabajador domain);

    FranjaDisponibilidadEmbeddable toFranjaEntity(FranjaDisponibilidad domain);

    FranjaDisponibilidad toFranjaDomain(FranjaDisponibilidadEmbeddable entity);

    List<FranjaDisponibilidadEmbeddable> toFranjasEntity(List<FranjaDisponibilidad> domain);
}
