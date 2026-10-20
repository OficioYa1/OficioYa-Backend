package com.oficioya.repository;

import com.oficioya.persistence.entity.ReferidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReferidoRepository extends JpaRepository<ReferidoEntity, Long> {
    long countByReferenteIdAndCuentaCreadaTrue(Long referenteId);
    Optional<ReferidoEntity> findByCorreoReferido(String correo);
}
