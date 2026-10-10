package com.oficioYa.repository;

import  com.oficioYa.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByCorreo(String correo);
    Optional<UsuarioEntity> findByCorreo(String correo);
}
