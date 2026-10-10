package com.oficioYa.repository;

import com.oficioYa.persistence.entity.ResenaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResenaRepository extends JpaRepository<ResenaEntity, Long> {

    List<ResenaEntity> findByReceptorId(Long receptorId);

    List<ResenaEntity> findByReceptorIdAndOficioId(Long receptorId, Long oficioId);

    @Query("SELECT AVG(r.estrellas) FROM ResenaEntity r WHERE r.receptor.id = :receptorId AND r.reportada = false")
    Optional<Double> calcularPromedioPorReceptor(@Param("receptorId") Long receptorId);

    @Query("SELECT AVG(r.estrellas) FROM ResenaEntity r WHERE r.receptor.id = :receptorId AND r.oficio.id = :oficioId AND r.reportada = false")
    Optional<Double> calcularPromedioPorReceptorYOficio(@Param("receptorId") Long receptorId, @Param("oficioId") Long oficioId);

    long countByReceptorIdAndReportadaFalse(Long receptorId);
}
