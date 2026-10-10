package com.oficioYa.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Document(collection = "portafolios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortafolioMongoDocument {
    @Id
    private String id;
    
    @Indexed
    private Long perfilTrabajadorId; // Referencia a la BD Postgres
    
    private List<String> fotosUrl;
    
    // OFY-66, OFY-67: Especializaciones por Oficio (Llave = oficioId as String)
    private java.util.Map<String, List<String>> especializacionesPorOficioId;
}
