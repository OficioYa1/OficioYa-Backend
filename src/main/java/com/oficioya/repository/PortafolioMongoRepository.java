package com.oficioya.repository;

import com.oficioya.persistence.document.PortafolioMongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortafolioMongoRepository extends MongoRepository<PortafolioMongoDocument, String> {
    Optional<PortafolioMongoDocument> findByPerfilTrabajadorId(Long perfilTrabajadorId);
}
