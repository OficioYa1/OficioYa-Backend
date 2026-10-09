package com.oficioya.repository;

import com.oficioya.persistence.entity.ReporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteEntity, Long> {
    List<ReporteEntity> findByReportadorId(Long reportadorId);
    List<ReporteEntity> findByReportadoId(Long reportadoId);
    List<ReporteEntity> findByEstado(com.oficioya.persistence.entity.EstadoReporte estado);
}
