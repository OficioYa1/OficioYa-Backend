package com.oficioya.repository;

import  com.oficioya.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    // Métodos findBy... se agregarán aquí en las ramas de features
    boolean existsByCorreo(String correo);
}
