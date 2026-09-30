package com.oficioya.repository;

import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilTrabajadorRepository extends JpaRepository<PerfilTrabajadorEntity, Long> {
    // Métodos findBy... o Specification se agregarán aquí en las ramas de features
}
