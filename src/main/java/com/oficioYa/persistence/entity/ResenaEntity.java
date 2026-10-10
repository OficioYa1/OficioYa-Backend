package com.oficioYa.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResenaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private UsuarioEntity autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id", nullable = false)
    private UsuarioEntity receptor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudEntity solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficio_id", nullable = false)
    private OficioEntity oficio;

    @Column(nullable = false)
    private Integer estrellas;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Builder.Default
    @Column(nullable = false)
    private boolean reportada = false;

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
