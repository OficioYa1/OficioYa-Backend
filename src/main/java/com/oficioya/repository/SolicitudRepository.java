package com.oficioya.repository;

import  com.oficioya.persistence.entity.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long> {
    
    // RF-44: Historial de solicitudes (Cliente)
    List<SolicitudEntity> findByContratanteIdOrderByFechaCreacionDesc(Long contratanteId);
    
    // RF-45: Historial de trabajos (Trabajador)
    List<SolicitudEntity> findByTrabajadorIdOrderByFechaCreacionDesc(Long trabajadorId);
    
    // RF-46 / RF-48: Estadisticas Cliente
    long countByContratanteIdAndEstado(Long contratanteId, com.oficioya.persistence.entity.EstadoSolicitud estado);
    long countByContratanteId(Long contratanteId);
    
    // RF-47 / RF-48: Estadisticas Trabajador
    long countByTrabajadorIdAndEstado(Long trabajadorId, com.oficioya.persistence.entity.EstadoSolicitud estado);
    long countByTrabajadorId(Long trabajadorId);
}
