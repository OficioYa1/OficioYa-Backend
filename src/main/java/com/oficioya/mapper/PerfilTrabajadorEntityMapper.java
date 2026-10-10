package com.oficioYa.mapper;

import com.oficioYa.model.domain.FranjaDisponibilidad;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.persistence.entity.FranjaDisponibilidadEmbeddable;
import com.oficioYa.persistence.entity.PerfilTrabajadorEntity;
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
