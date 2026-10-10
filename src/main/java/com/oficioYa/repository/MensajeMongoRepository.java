package com.oficioYa.repository;

import com.oficioYa.persistence.document.MensajeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeMongoRepository extends MongoRepository<MensajeDocument, String> {
    
    // Obtener los mensajes de una solicitud específica ordenados por fecha
    List<MensajeDocument> findBySolicitudIdOrderByFechaEnvioAsc(Long solicitudId);
    
    // Marcar como leídos los mensajes no leídos para un destinatario en una solicitud
    // (Esto normalmente se hace con MongoTemplate para updateMulti, pero para simplicidad 
    // lo podemos buscar y actualizar en el service)
    List<MensajeDocument> findBySolicitudIdAndDestinatarioIdAndLeidoFalse(Long solicitudId, Long destinatarioId);
}
