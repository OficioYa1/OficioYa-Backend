package com.oficioya.repository;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PerfilTrabajadorRepository extends JpaRepository<PerfilTrabajadorEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT p FROM PerfilTrabajadorEntity p WHERE :zona IS NULL OR p.zonaCobertura LIKE %:zona%")
    java.util.List<PerfilTrabajadorEntity> findByZonaCoberturaContaining(@org.springframework.data.repository.query.Param("zona") String zona);
    Optional<PerfilTrabajadorEntity> findByUsuarioId(Long usuarioId);
}
