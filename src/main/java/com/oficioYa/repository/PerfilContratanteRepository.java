package com.oficioYa.repository;

import com.oficioYa.persistence.entity.PerfilContratanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilContratanteRepository extends JpaRepository<PerfilContratanteEntity, Long> {
    Optional<PerfilContratanteEntity> findByUsuarioId(Long usuarioId);
}
