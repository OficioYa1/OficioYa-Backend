package com.oficioYa.repository;

import  com.oficioYa.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    // Métodos findBy... se agregarán aquí en las ramas de features
    boolean existsByCorreo(String correo);
}
