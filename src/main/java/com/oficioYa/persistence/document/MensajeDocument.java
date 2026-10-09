package com.oficioya.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mensajes")
public class MensajeDocument {

    @Id
    private String id;

    @Field("solicitud_id")
    private Long solicitudId; // Para agrupar los mensajes por solicitud

    @Field("remitente_id")
    private Long remitenteId;

    @Field("destinatario_id")
    private Long destinatarioId;

    @Field("contenido")
    private String contenido;

    @Builder.Default
    @Field("fecha_envio")
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @Builder.Default
    @Field("leido")
    private boolean leido = false;
}
