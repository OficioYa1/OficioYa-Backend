package com.oficioya.repository;

import com.oficioya.persistence.entity.OficioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OficioRepository extends JpaRepository<OficioEntity, Long> {
}
